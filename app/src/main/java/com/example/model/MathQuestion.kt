package com.example.model

enum class MathOperator(val symbol: String) {
  PLUS("+"),
  MINUS("-")
}

enum class TangibleItemType(val titleAr: String, val singularAr: String) {
  SHINY_APPLE("تفاح أحمر لذيذ", "تفاحة"),
  GOLDEN_STAR("نجوم ذهبية لامعة", "نجمة"),
  MAGIC_CRYSTAL("بلورات طاقة سحرية", "بلورة"),
  ENERGY_BATTERY("بطاريات فضائية قوية", "بطارية"),
  GOLD_COIN("عملات ذهبية براقة", "عملة")
}

data class MathQuestion(
  val id: String,
  val level: Int,
  val firstCount: Int,
  val secondCount: Int,
  val operator: MathOperator = MathOperator.PLUS,
  val item: TangibleItemType,
  val promptAr: String,
  val options: List<Int>,
  val correctAnswer: Int
) {
  companion object {
    fun generateQuestionsForLevel(level: Int, count: Int = 5): List<MathQuestion> {
      val questions = mutableListOf<MathQuestion>()
      val items = TangibleItemType.values()

      when (level) {
        1 -> {
          // Numbers 1 to 5: High visual appeal, simple counting
          val pairs = listOf(
            Pair(1, 1), Pair(1, 2), Pair(2, 1), Pair(2, 2),
            Pair(3, 1), Pair(1, 3), Pair(2, 3), Pair(3, 2),
            Pair(4, 1), Pair(1, 4)
          ).shuffled()

          val prompts = listOf(
            "كم تفاحة لذيذة معنا في السلة؟",
            "اجمع النجوم الذهبية وأخبرني بالعدد!",
            "كم بطارية طاقة لدينا لتشغيل المركبة؟",
            "عد البلورات السحرية واكتشف المجموع!",
            "كم عملة ذهبية في الكنز؟"
          )

          for (i in 0 until count) {
            val (a, b) = pairs[i % pairs.size]
            val answer = a + b
            val item = items[i % items.size]
            val prompt = prompts[i % prompts.size]
            val options = generateOptions(answer, minVal = 1, maxVal = 6, optionCount = 3)
            questions.add(
              MathQuestion(
                id = "lvl1_$i",
                level = 1,
                firstCount = a,
                secondCount = b,
                operator = MathOperator.PLUS,
                item = item,
                promptAr = prompt,
                options = options,
                correctAnswer = answer
              )
            )
          }
        }
        2 -> {
          // Numbers up to 10: Addition and Subtraction
          val pool = mutableListOf<Triple<Int, Int, MathOperator>>()
          listOf(
            Pair(3, 3), Pair(4, 2), Pair(5, 3), Pair(4, 4),
            Pair(5, 4), Pair(6, 2), Pair(5, 5), Pair(7, 2),
            Pair(3, 5), Pair(6, 3)
          ).forEach { pool.add(Triple(it.first, it.second, MathOperator.PLUS)) }

          listOf(
            Pair(5, 2), Pair(6, 3), Pair(7, 2), Pair(8, 3),
            Pair(9, 4), Pair(6, 2), Pair(7, 4), Pair(8, 4)
          ).forEach { pool.add(Triple(it.first, it.second, MathOperator.MINUS)) }

          pool.shuffle()
          for (i in 0 until count) {
            val (a, b, op) = pool[i % pool.size]
            val answer = if (op == MathOperator.PLUS) a + b else a - b
            val item = items[(i + 1) % items.size]
            val prompt = if (op == MathOperator.PLUS) {
              "أضف المجموعتين معاً واكتشف الناتج!"
            } else {
              "أنقصنا بعض العناصر.. كم تبقى معنا الآن؟"
            }
            val options = generateOptions(answer, minVal = 1, maxVal = 10, optionCount = 3)
            questions.add(
              MathQuestion(
                id = "lvl2_$i",
                level = 2,
                firstCount = a,
                secondCount = b,
                operator = op,
                item = item,
                promptAr = prompt,
                options = options,
                correctAnswer = answer
              )
            )
          }
        }
        3 -> {
          // Numbers up to 20: Champion challenges
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
            val item = items[(i + 3) % items.size]
            val prompt = if (op == MathOperator.PLUS) {
              "تحدي الأبطال الكبير: ما هو المجموع الكلي؟"
            } else {
              "تحدي الطرح الذكي: احسب الباقي بدقة!"
            }
            val options = generateOptions(answer, minVal = 1, maxVal = 20, optionCount = 4)
            questions.add(
              MathQuestion(
                id = "lvl3_$i",
                level = 3,
                firstCount = a,
                secondCount = b,
                operator = op,
                item = item,
                promptAr = prompt,
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
      var fallback = minVal
      while (options.size < optionCount && fallback <= maxVal) {
        options.add(fallback)
        fallback++
      }
      return options.toList().shuffled()
    }
  }
}
