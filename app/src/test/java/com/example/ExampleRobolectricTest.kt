package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.MathQuestion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
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
  fun `math question generator produces valid questions for all levels`() {
    for (lvl in 1..3) {
      val questions = MathQuestion.generateQuestionsForLevel(lvl, count = 5)
      assertEquals(5, questions.size)
      for (q in questions) {
        assertTrue(q.options.contains(q.correctAnswer))
        assertTrue(q.firstCount > 0)
        assertTrue(q.secondCount > 0)
        assertTrue(q.correctAnswer > 0)
      }
    }
  }
}
