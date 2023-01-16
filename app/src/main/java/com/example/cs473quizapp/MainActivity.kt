package com.example.cs473quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), Quiz.QuizListener, Score.ResultListener {
    private val questions: List<String> = listOf(
        "The USA is the ___ largest country on the world.#3rd#4th#5th#6th#7th",
        "How many stripes does the USA flag has?#10#11#12#13#14",
        "Which is the largest city in the USA?#Chicago#New York#Huston#Virginia#Los Angeles",
        "The most populous U.S. state is ___.#California#Texas#Alaska#Florida#",
        "The largest U.S. state by land is ___.#Texas#Arizona#Alaska#New Mexico#Montana",
        "A sneeze is often faster than ___mph.#90#120#110#105#100",
        "___% of all the oxygen you breathe is used by your brain.#10#15#20#25#50",
        "Our solar system has ___ planet.#10#9#11#7#8",
        "___% of earth's surface is covered in water.#51#61#71#49.78#67.44",
        "Earth is ___ billion years old#4#4.5#4.7#4.85#4.3",
        "Moon is ___ miles away from earth#300,000#250,340#238,000#405,000#120,900",
        "The circumference is almost ___ miles!#12,000#14,000#20,200#25,000#30,000",
        "Earth’s atmosphere has ___ layers#6#4#7#2#5",
        "You blink over ___ million times in a single year.#14#16#15#18#17",
        "Lightning strikes about ___ times per minute on earth.#4,500#6,000#6,500#6,800#7,000"

    )
    private val answers: List<Int> = listOf(
        1,4,2,1,3,5,3,5,3,2,3,4,1,2,3
    )

    private val chosenAnswers = mutableListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var currentQuestion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openQuiz(questions[0], chosenAnswers[0], currentQuestion)
    }

    private fun openQuiz(dataQuestions: String, checkedOption: Int, questionNumber: Int) {
        val firstFragment = Quiz.newInstance(dataQuestions, checkedOption, questionNumber)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, firstFragment)
        transaction.commit()
    }

    private fun openResultFragment(result: String, description: String) {
        val secondFragment = Score.newInstance(result, description)
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, secondFragment)
        transaction.commit()
    }

    override fun back() {
        currentQuestion = 0
        chosenAnswers.fill(0)
        openQuiz(questions[currentQuestion], chosenAnswers[currentQuestion], currentQuestion)
    }

    override fun next(chosenOption: Int, questionNumber: Int) {
        chosenAnswers[questionNumber] = chosenOption
        currentQuestion = questionNumber + 1
        if (questionNumber == questions.size - 1) {
            openResultFragment(generateResult(), generateDescription())
        } else {
            openQuiz(questions[currentQuestion], chosenAnswers[currentQuestion], currentQuestion)
        }
    }

    override fun previous(chosenOption: Int, questionNumber: Int) {
        chosenAnswers[questionNumber] = chosenOption
        currentQuestion = questionNumber - 1
        openQuiz(questions[currentQuestion], chosenAnswers[currentQuestion], currentQuestion)
    }

    private fun generateResult(): String {
        var counter = 0;
        chosenAnswers.forEachIndexed { index, i ->
            if (i == answers[index]) {
                counter++
            }
        }
        val percent: Long = counter.toLong()  * 100 / questions.size.toLong()
        return "Your result: $percent %"
    }

    private fun generateDescription(): String {
        val description = StringBuilder()
        description.append(generateResult() + "\n\n")
        chosenAnswers.forEachIndexed { index, i ->
            val question = questions[index].substringBefore('#')
            val answer = questions[index].split('#')[i]
            description.append("${index+1}) $question\nYour answer: $answer \n\n")
        }
        return description.toString()
    }
}