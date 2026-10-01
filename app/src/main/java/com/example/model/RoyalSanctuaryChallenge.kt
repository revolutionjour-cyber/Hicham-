package com.example.model

import com.example.ui.components.LuxuryGemType

data class RoyalSanctuaryChallenge(
  val id: String,
  val chamberLevel: Int,
  val gemType: LuxuryGemType,
  val leftTargetWeight: Int,
  val initialRightWeight: Int,
  val storyDialogueAr: String,
  val options: List<Int>,
  val rewardArtifactName: String
) {
  val neededToAdd: Int
    get() = leftTargetWeight - initialRightWeight

  val correctAnswer: Int
    get() = neededToAdd

  companion object {
    fun generateChallengesForChamber(chamber: Int, count: Int = 5): List<RoyalSanctuaryChallenge> {
      val list = mutableListOf<RoyalSanctuaryChallenge>()

      when (chamber) {
        1 -> {
          // Chamber 1: The Imperial Ruby Vault (Numbers 1 to 5)
          val setups = listOf(
            Triple(3, 1, "تاج الياقوت الملكي"),
            Triple(4, 2, "خاتم النور الذهبي"),
            Triple(5, 3, "قلادة الزمرد الصافي"),
            Triple(4, 1, "صولجان الشجاعة"),
            Triple(5, 2, "صندوق المجوهرات الفاره")
          )
          for (i in 0 until count) {
            val (target, initial, reward) = setups[i % setups.size]
            val needed = target - initial
            val prompt = "في الكفة اليسرى $target من حبات الياقوت، ولديك $initial.. كم حبة تزن الميزان؟"
            val options = generateDistinctOptions(needed, 1..5, 3)
            list.add(
              RoyalSanctuaryChallenge(
                id = "ch1_$i",
                chamberLevel = 1,
                gemType = LuxuryGemType.RUBY,
                leftTargetWeight = target,
                initialRightWeight = initial,
                storyDialogueAr = prompt,
                options = options,
                rewardArtifactName = reward
              )
            )
          }
        }
        2 -> {
          // Chamber 2: The Royal Emerald Atrium (Numbers 1 to 10)
          val setups = listOf(
            Triple(7, 3, "ساعة الزمن الذهبية"),
            Triple(8, 4, "مرآة الحكمة الكريستالية"),
            Triple(6, 2, "مفتاح الخزنة الملكية"),
            Triple(9, 5, "بروش الفيروز المرصع"),
            Triple(10, 6, "تاج الأمراء العظيم")
          )
          for (i in 0 until count) {
            val (target, initial, reward) = setups[i % setups.size]
            val needed = target - initial
            val prompt = "الكفة اليسرى تحمل $target حبات زمرد! كم جوهرة نضع في الكفة اليمنى حتى يتساويا؟"
            val options = generateDistinctOptions(needed, 1..10, 3)
            list.add(
              RoyalSanctuaryChallenge(
                id = "ch2_$i",
                chamberLevel = 2,
                gemType = LuxuryGemType.EMERALD,
                leftTargetWeight = target,
                initialRightWeight = initial,
                storyDialogueAr = prompt,
                options = options,
                rewardArtifactName = reward
              )
            )
          }
        }
        3 -> {
          // Chamber 3: The Grand Diamond & Sapphire Pavilion (Numbers 1 to 15)
          val setups = listOf(
            Triple(12, 7, "كرة النجوم السحرية"),
            Triple(14, 8, "التاج الإمبراطوري المرصع"),
            Triple(15, 9, "خنجر الذهب الخالص"),
            Triple(11, 6, "إبريق الكريستال الملكي"),
            Triple(13, 5, "درع الفرسان الذهبي")
          )
          for (i in 0 until count) {
            val (target, initial, reward) = setups[i % setups.size]
            val needed = target - initial
            val prompt = "سر الميزان الملكي الأعظم! زن $target من الجواهر، لديك $initial، كم يتبقى لتنال الكنز؟"
            val options = generateDistinctOptions(needed, 1..15, 4)
            list.add(
              RoyalSanctuaryChallenge(
                id = "ch3_$i",
                chamberLevel = 3,
                gemType = LuxuryGemType.SAPPHIRE,
                leftTargetWeight = target,
                initialRightWeight = initial,
                storyDialogueAr = prompt,
                options = options,
                rewardArtifactName = reward
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
