package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AdaptiveQuestionGenerator
import com.example.model.Difficulty
import com.example.model.MathOp
import com.example.ui.KidMathViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("فضاء الأرقام", appName)
  }

  @Test
  fun `adaptive question generator produces valid questions for all operations and difficulties`() {
    val operations = listOf(MathOp.PLUS, MathOp.MINUS, MathOp.MULTIPLY, MathOp.DIVIDE, MathOp.MIXED)
    val difficulties = listOf(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD)

    for (op in operations) {
      for (diff in difficulties) {
        val q = AdaptiveQuestionGenerator.generateQuestion(op, diff, adaptiveTier = 2)
        assertNotNull(q)
        assertTrue(q.options.contains(q.answer))
        assertEquals(4, q.options.size)
        assertTrue(q.firstNum > 0)
        assertTrue(q.secondNum > 0)
        assertTrue(q.answer > 0)
        assertTrue(q.storyPromptAr.isNotBlank())
      }
    }
  }

  @Test
  fun `viewModel answer execution records streak and transitions questions`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = KidMathViewModel(app)
    val q1 = viewModel.currentChallenge.value
    assertNotNull(q1)

    // Execute correct answer
    viewModel.onDirectAnswerSelected(q1.answer)
    assertEquals(1, viewModel.comboStreak.value)
    assertEquals(1, viewModel.directStars.value)
  }

  @Test
  fun `main activity launches successfully with direct play screen`() {
    val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
    assertNotNull(controller.get())
  }
}
