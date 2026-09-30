package com.example.model

enum class MathOperator(val symbol: String) {
  PLUS("+"),
  MINUS("-")
}

enum class CosmicItem(val emoji: String, val nameAr: String) {
  STAR("⭐", "نجوم"),
  ROCKET("🚀", "صواريخ"),
  PLANET("🪐", "كواكب"),
  UFO("🛸", "أطباق طائرة"),
  ALIEN("👾", "كائنات فضائية"),
  MOON("🌕", "أقمار"),
  GEM("💎", "جواهر"),
  SPARKLE("✨", "بريق")
}

data class MathQuestion(
  val id: String,
  val level: Int,
  val firstCount: Int,
  val secondCount: Int,
  val operator: MathOperator = MathOperator.PLUS,
  val item: CosmicItem,
  val options: List<Int>,
  val correctAnswer: Int
) {
  companion object {
    fun generateQuestionsForLevel(level: Int, count: Int = 5): List<MathQuestion> {
      val questions = mutableListOf<MathQuestion>()
      val items = CosmicItem.values()

      when (level) {
        1 -> {
          // Numbers 1 to 5. Pure addition, highly visual
          val pairs = listOf(
            Pair(1, 1), Pair(1, 2), Pair(2, 1), Pair(2, 2),
            Pair(3, 1), Pair(1, 3), Pair(2, 3), Pair(3, 2),
            Pair(4, 1), Pair(1, 4)
          ).shuffled()

          for (i in 0 until count) {
            val (a, b) = pairs[i % pairs.size]
            val answer = a + b
            val item = items[i % 3] // STAR, ROCKET, PLANET
            val options = generateOptions(answer, minVal = 1, maxVal = 6, optionCount = 3)
            questions.add(
              MathQuestion(
                id = "lvl1_$i",
                level = 1,
                firstCount = a,
                secondCount = b,
                operator = MathOperator.PLUS,
                item = item,
                options = options,
                correctAnswer = answer
              )
            )
          }
        }
        2 -> {
          // Numbers up to 10. Addition & Subtraction
          val pool = mutableListOf<Triple<Int, Int, MathOperator>>()
          // Addition up to 10
          listOf(
            Pair(3, 3), Pair(4, 2), Pair(5, 3), Pair(4, 4),
            Pair(5, 4), Pair(6, 2), Pair(5, 5), Pair(7, 2),
            Pair(3, 5), Pair(6, 3)
          ).forEach { pool.add(Triple(it.first, it.second, MathOperator.PLUS)) }
          // Subtraction
          listOf(
            Pair(5, 2), Pair(6, 3), Pair(7, 2), Pair(8, 3),
            Pair(9, 4), Pair(6, 2), Pair(7, 4), Pair(8, 4)
          ).forEach { pool.add(Triple(it.first, it.second, MathOperator.MINUS)) }

          pool.shuffle()
          for (i in 0 until count) {
            val (a, b, op) = pool[i % pool.size]
            val answer = if (op == MathOperator.PLUS) a + b else a - b
            val item = items[(i + 3) % items.size]
            val options = generateOptions(answer, minVal = 1, maxVal = 10, optionCount = 3)
            questions.add(
              MathQuestion(
                id = "lvl2_$i",
                level = 2,
                firstCount = a,
                secondCount = b,
                operator = op,
                item = item,
                options = options,
                correctAnswer = answer
              )
            )
          }
        }
        3 -> {
          // Numbers up to 15 / 20. Advanced cosmic challenges
          val pool = listOf(
            Triple(6, 5, MathOperator.PLUS),
            Triple(7, 6, MathOperator.PLUS),
            Triple(8, 4, MathOperator.PLUS),
            Triple(9, 5, MathOperator.PLUS),
            Triple(10, 4, MathOperator.PLUS),
            Triple(12, 4, MathOperator.MINUS),
            Triple(11, 5, MathOperator.MINUS),
            Triple(13, 6, MathOperator.MINUS),
            Triple(7, 7, MathOperator.PLUS),
            Triple(8, 7, MathOperator.PLUS)
          ).shuffled()

          for (i in 0 until count) {
            val (a, b, op) = pool[i % pool.size]
            val answer = if (op == MathOperator.PLUS) a + b else a - b
            val item = items[(i + 5) % items.size]
            val options = generateOptions(answer, minVal = 1, maxVal = 20, optionCount = 4)
            questions.add(
              MathQuestion(
                id = "lvl3_$i",
                level = 3,
                firstCount = a,
                secondCount = b,
                operator = op,
                item = item,
                options = options,
                correctAnswer = answer
              )
            )
          }
        }
      }
      return questions
    }

    private fun generateOptions(correct: Int, minVal: Int, maxVal: Int, optionCount: Int): List<Int> {
      val options = mutableSetOf(correct)
      val candidates = mutableListOf<Int>()
      // Prefer close distractors (±1, ±2)
      for (offset in listOf(-1, 1, -2, 2, -3, 3)) {
        val cand = correct + offset
        if (cand in minVal..maxVal && cand != correct) {
          candidates.add(cand)
        }
      }
      candidates.shuffle()
      for (c in candidates) {
        options.add(c)
        if (options.size == optionCount) break
      }
      // Fill remaining if needed
      var fallback = minVal
      while (options.size < optionCount && fallback <= maxVal) {
        options.add(fallback)
        fallback++
      }
      return options.toList().shuffled()
    }
  }
}
