package com.example.model

import com.example.ui.components.ClayFoodType

data class MonsterMathChallenge(
  val id: String,
  val level: Int,
  val foodType: ClayFoodType,
  val initialInBowl: Int,
  val targetTotal: Int,
  val isSubtraction: Boolean = false,
  val storyPromptAr: String,
  val options: List<Int>
) {
  val neededToFeed: Int
    get() = if (isSubtraction) initialInBowl - targetTotal else targetTotal - initialInBowl

  val correctAnswer: Int
    get() = if (isSubtraction) targetTotal else neededToFeed

  companion object {
    fun generateChallengesForLevel(level: Int, count: Int = 5): List<MonsterMathChallenge> {
      val list = mutableListOf<MonsterMathChallenge>()
      val foods = ClayFoodType.values()

      when (level) {
        1 -> {
          // Numbers 1 to 5: Intuitive feeding addition
          val configs = listOf(
            Triple(1, 3, ClayFoodType.STRAWBERRY), // Has 1, wants 3 -> feed 2
            Triple(2, 4, ClayFoodType.CUPCAKE),    // Has 2, wants 4 -> feed 2
            Triple(1, 4, ClayFoodType.DONUT),      // Has 1, wants 4 -> feed 3
            Triple(2, 5, ClayFoodType.STAR_CANDY), // Has 2, wants 5 -> feed 3
            Triple(3, 5, ClayFoodType.STRAWBERRY), // Has 3, wants 5 -> feed 2
            Triple(1, 5, ClayFoodType.CUPCAKE)     // Has 1, wants 5 -> feed 4
          ).shuffled()

          for (i in 0 until count) {
            val (init, target, food) = configs[i % configs.size]
            val needed = target - init
            val prompt = "بوبو الجائع يريد $target ${food.titleAr}! لديه في الصحن $init، كم نضيف له ليشبع؟"
            val options = generateDistinctOptions(needed, 1..5, 3)
            list.add(
              MonsterMathChallenge(
                id = "lvl1_$i",
                level = 1,
                foodType = food,
                initialInBowl = init,
                targetTotal = target,
                isSubtraction = false,
                storyPromptAr = prompt,
                options = options
              )
            )
          }
        }
        2 -> {
          // Numbers up to 10: Mixed feeding and sharing (Addition and Subtraction)
          val configs = listOf(
            Triple(3, 7, false),  // 3 + 4 = 7
            Triple(4, 8, false),  // 4 + 4 = 8
            Triple(5, 9, false),  // 5 + 4 = 9
            Triple(2, 6, false),  // 2 + 4 = 6
            Triple(8, 5, true),   // Has 8, gives away 3 -> stays 5
            Triple(7, 4, true),   // Has 7, gives away 3 -> stays 4
            Triple(9, 6, true),   // Has 9, gives away 3 -> stays 6
            Triple(6, 3, true)    // Has 6, gives away 3 -> stays 3
          ).shuffled()

          for (i in 0 until count) {
            val (init, target, isSub) = configs[i % configs.size]
            val food = foods[(i + 1) % foods.size]
            val (prompt, correct) = if (!isSub) {
              val needed = target - init
              Pair("بوبو يريد $target من ${food.titleAr}! يوجد بالصحن $init، كم حبة نطعمه؟", needed)
            } else {
              val toGive = init - target
              Pair("في صحن بوبو $init من ${food.titleAr}، أكل منها $toGive! كم حبة بقيت لديه؟", target)
            }
            val options = generateDistinctOptions(correct, 1..10, 3)
            list.add(
              MonsterMathChallenge(
                id = "lvl2_$i",
                level = 2,
                foodType = food,
                initialInBowl = init,
                targetTotal = target,
                isSubtraction = isSub,
                storyPromptAr = prompt,
                options = options
              )
            )
          }
        }
        3 -> {
          // Numbers up to 15: Big Monster Feast challenges!
          val configs = listOf(
            Triple(6, 12, false),
            Triple(7, 14, false),
            Triple(8, 15, false),
            Triple(5, 11, false),
            Triple(13, 8, true),
            Triple(14, 9, true),
            Triple(12, 7, true),
            Triple(15, 10, true)
          ).shuffled()

          for (i in 0 until count) {
            val (init, target, isSub) = configs[i % configs.size]
            val food = foods[(i + 2) % foods.size]
            val (prompt, correct) = if (!isSub) {
              val needed = target - init
              Pair("وليمة بوبو الكبرى! الهدف هو $target حبة، ولديه $init.. كم نضيف؟", needed)
            } else {
              val toGive = init - target
              Pair("شارك بوبو أصدقاءه $toGive حبات من أصل $init! كم حبة باقية في طبقه؟", target)
            }
            val options = generateDistinctOptions(correct, 1..15, 4)
            list.add(
              MonsterMathChallenge(
                id = "lvl3_$i",
                level = 3,
                foodType = food,
                initialInBowl = init,
                targetTotal = target,
                isSubtraction = isSub,
                storyPromptAr = prompt,
                options = options
              )
            )
          }
        }
      }
      return list
    }

    private fun generateDistinctOptions(correct: Int, range: IntRange, count: Int): List<Int> {
      val set = mutableSetOf(correct)
      val pool = listOf(correct - 1, correct + 1, correct - 2, correct + 2, correct - 3, correct + 3)
        .filter { it in range && it != correct }
        .shuffled()

      for (cand in pool) {
        set.add(cand)
        if (set.size == count) break
      }
      for (fallback in range) {
        if (set.size == count) break
        set.add(fallback)
      }
      return set.toList().shuffled()
    }
  }
}
