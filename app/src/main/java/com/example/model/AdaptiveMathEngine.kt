package com.example.model

import kotlin.random.Random

enum class MathOp(val symbol: String, val titleAr: String) {
  PLUS("+", "الجمع"),
  MINUS("-", "الطرح"),
  MULTIPLY("×", "الضرب"),
  DIVIDE("÷", "القسمة"),
  MIXED("🔀", "شامل")
}

enum class Difficulty(val titleAr: String, val rangeLabel: String, val colorHex: Long) {
  EASY("سهل", "١ - ٥", 0xFF10B981),
  MEDIUM("متوسط", "١ - ٢٠", 0xFF0284C7),
  HARD("صعب", "١ - ١٠٠", 0xFFD97706)
}

data class DynamicMathChallenge(
  val id: String,
  val firstNum: Int,
  val secondNum: Int,
  val effectiveOp: MathOp,
  val answer: Int,
  val options: List<Int>,
  val storyPromptAr: String,
  val itemType: TangibleItemType,
  val difficulty: Difficulty,
  val adaptiveTier: Int
)

object AdaptiveQuestionGenerator {
  // Recent questions cache to guarantee no repetition
  private val recentQuestionSignatures = mutableListOf<String>()
  private const val HISTORY_LIMIT = 25

  fun generateQuestion(
    chosenOp: MathOp,
    difficulty: Difficulty,
    adaptiveTier: Int // e.g. 1..5 scaling automatically with streak
  ): DynamicMathChallenge {
    val items = TangibleItemType.values()
    val randomItem = items.random()

    var attempts = 0
    while (attempts < 50) {
      attempts++

      // If MIXED is chosen, pick one of the 4 operators at random
      val op = if (chosenOp == MathOp.MIXED) {
        listOf(MathOp.PLUS, MathOp.MINUS, MathOp.MULTIPLY, MathOp.DIVIDE).random()
      } else {
        chosenOp
      }

      val (a, b, ans, prompt) = when (difficulty) {
        Difficulty.EASY -> generateEasy(op, adaptiveTier, randomItem)
        Difficulty.MEDIUM -> generateMedium(op, adaptiveTier, randomItem)
        Difficulty.HARD -> generateHard(op, adaptiveTier, randomItem)
      }

      val signature = "$a${op.symbol}$b"
      if (!recentQuestionSignatures.contains(signature) || attempts >= 40) {
        recentQuestionSignatures.add(signature)
        if (recentQuestionSignatures.size > HISTORY_LIMIT) {
          recentQuestionSignatures.removeAt(0)
        }

        val options = generateDistractors(ans, difficulty)
        return DynamicMathChallenge(
          id = "q_${System.currentTimeMillis()}_$attempts",
          firstNum = a,
          secondNum = b,
          effectiveOp = op,
          answer = ans,
          options = options,
          storyPromptAr = prompt,
          itemType = randomItem,
          difficulty = difficulty,
          adaptiveTier = adaptiveTier
        )
      }
    }

    // Fallback if loop exhausted
    return DynamicMathChallenge(
      id = "fallback_${System.currentTimeMillis()}",
      firstNum = 2,
      secondNum = 2,
      effectiveOp = MathOp.PLUS,
      answer = 4,
      options = listOf(3, 4, 5, 6),
      storyPromptAr = "احسب ناتج: ٢ + ٢",
      itemType = TangibleItemType.SHINY_APPLE,
      difficulty = difficulty,
      adaptiveTier = adaptiveTier
    )
  }

  private fun generateEasy(op: MathOp, tier: Int, item: TangibleItemType): Quad<Int, Int, Int, String> {
    val itemName = item.titleAr
    return when (op) {
      MathOp.PLUS -> {
        val a = Random.nextInt(1, 4 + tier)
        val b = Random.nextInt(1, 4 + tier)
        val ans = a + b
        Quad(a, b, ans, "لدينا $a من $itemName، وأضفنا إليها $b.. كم أصبح المجموع؟")
      }
      MathOp.MINUS -> {
        val a = Random.nextInt(3, 6 + tier)
        val b = Random.nextInt(1, a)
        val ans = a - b
        Quad(a, b, ans, "كان في السلة $a من $itemName، أخذنا منها $b.. كم بقي؟")
      }
      MathOp.MULTIPLY -> {
        val a = Random.nextInt(1, 3 + (tier / 2))
        val b = Random.nextInt(1, 3)
        val ans = a * b
        Quad(a, b, ans, "$a مجموعات، في كل مجموعة $b من $itemName.. كم العدد الكلي؟")
      }
      MathOp.DIVIDE, MathOp.MIXED -> {
        val b = Random.nextInt(1, 3)
        val quotient = Random.nextInt(1, 3 + (tier / 2))
        val a = b * quotient
        Quad(a, b, quotient, "وزّعنا $a من $itemName بالتساوي على $b أصدقاء.. كم يأخذ كل واحد؟")
      }
    }
  }

  private fun generateMedium(op: MathOp, tier: Int, item: TangibleItemType): Quad<Int, Int, Int, String> {
    val itemName = item.titleAr
    return when (op) {
      MathOp.PLUS -> {
        val a = Random.nextInt(5 + tier, 12 + tier * 2)
        val b = Random.nextInt(3 + tier, 10 + tier * 2)
        val ans = a + b
        Quad(a, b, ans, "احسب ناتج جمع: $a + $b")
      }
      MathOp.MINUS -> {
        val a = Random.nextInt(10 + tier * 2, 20 + tier * 2)
        val b = Random.nextInt(3, a - 2)
        val ans = a - b
        Quad(a, b, ans, "ما هو ناتج طرح: $a - $b ؟")
      }
      MathOp.MULTIPLY -> {
        val a = Random.nextInt(2, 6 + tier)
        val b = Random.nextInt(2, 5 + tier)
        val ans = a * b
        Quad(a, b, ans, "ما حاصل ضرب: $a × $b ؟")
      }
      MathOp.DIVIDE, MathOp.MIXED -> {
        val b = Random.nextInt(2, 6)
        val quotient = Random.nextInt(2, 6 + tier)
        val a = b * quotient
        Quad(a, b, quotient, "اقسم $a على $b، ما هو الناتج؟")
      }
    }
  }

  private fun generateHard(op: MathOp, tier: Int, item: TangibleItemType): Quad<Int, Int, Int, String> {
    return when (op) {
      MathOp.PLUS -> {
        val a = Random.nextInt(15 + tier * 5, 45 + tier * 10)
        val b = Random.nextInt(15 + tier * 5, 45 + tier * 10)
        val ans = a + b
        Quad(a, b, ans, "تحدي الأبطال: احسب مجموع $a + $b")
      }
      MathOp.MINUS -> {
        val a = Random.nextInt(40 + tier * 10, 99)
        val b = Random.nextInt(15, a - 10)
        val ans = a - b
        Quad(a, b, ans, "تحدي العباقرة: احسب $a - $b")
      }
      MathOp.MULTIPLY -> {
        val a = Random.nextInt(6, 12)
        val b = Random.nextInt(4, 11)
        val ans = a * b
        Quad(a, b, ans, "جدول الضرب المتقدم: ما ناتج $a × $b ؟")
      }
      MathOp.DIVIDE, MathOp.MIXED -> {
        val b = Random.nextInt(4, 10)
        val quotient = Random.nextInt(4, 12)
        val a = b * quotient
        Quad(a, b, quotient, "القسمة السريعة: $a ÷ $b = ؟")
      }
    }
  }

  private fun generateDistractors(correct: Int, difficulty: Difficulty): List<Int> {
    val result = mutableSetOf(correct)
    val offsets = when (difficulty) {
      Difficulty.EASY -> listOf(-1, 1, -2, 2, 3, -3)
      Difficulty.MEDIUM -> listOf(-1, 1, -2, 2, -5, 5, -10, 10)
      Difficulty.HARD -> listOf(-1, 1, -10, 10, -2, 2, -5, 5, -20, 20)
    }.shuffled()

    for (off in offsets) {
      val cand = correct + off
      if (cand > 0 && cand != correct) {
        result.add(cand)
      }
      if (result.size == 4) break
    }

    // Fallback if needed
    var fallback = 1
    while (result.size < 4) {
      val candidate = (correct + fallback).coerceAtLeast(1)
      if (candidate != correct) {
        result.add(candidate)
      }
      fallback++
    }

    return result.toList().shuffled()
  }

  private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
