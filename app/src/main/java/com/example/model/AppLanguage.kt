package com.example.model

enum class AppLanguage(
  val code: String,
  val displayName: String,
  val flag: String,
  val isRtl: Boolean
) {
  MOROCCAN_ARABIC("ar-MA", "الدارجة المغربية", "🇲🇦", true),
  FRENCH("fr", "Français", "🇫🇷", false),
  ENGLISH("en", "English", "🇬🇧", false)
}

object LanguageStrings {
  fun getOpTitle(op: MathOp, lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> when (op) {
      MathOp.PLUS -> "الجمع +"
      MathOp.MINUS -> "الناقص −"
      MathOp.MULTIPLY -> "الضرب ×"
      MathOp.DIVIDE -> "القسمة ÷"
      MathOp.MIXED -> "مخلط 🔀"
    }
    AppLanguage.FRENCH -> when (op) {
      MathOp.PLUS -> "Addition +"
      MathOp.MINUS -> "Soustraction −"
      MathOp.MULTIPLY -> "Multiplication ×"
      MathOp.DIVIDE -> "Division ÷"
      MathOp.MIXED -> "Mixte 🔀"
    }
    AppLanguage.ENGLISH -> when (op) {
      MathOp.PLUS -> "Addition +"
      MathOp.MINUS -> "Subtraction −"
      MathOp.MULTIPLY -> "Multiplication ×"
      MathOp.DIVIDE -> "Division ÷"
      MathOp.MIXED -> "Mixed 🔀"
    }
  }

  fun getDifficultyTitle(diff: Difficulty, lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> when (diff) {
      Difficulty.EASY -> "ساهل"
      Difficulty.MEDIUM -> "متوسط"
      Difficulty.HARD -> "صعيب"
    }
    AppLanguage.FRENCH -> when (diff) {
      Difficulty.EASY -> "Facile"
      Difficulty.MEDIUM -> "Moyen"
      Difficulty.HARD -> "Difficile"
    }
    AppLanguage.ENGLISH -> when (diff) {
      Difficulty.EASY -> "Easy"
      Difficulty.MEDIUM -> "Medium"
      Difficulty.HARD -> "Hard"
    }
  }

  fun getDifficultyRange(diff: Difficulty, lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> diff.rangeLabel
    AppLanguage.FRENCH -> when (diff) {
      Difficulty.EASY -> "1 - 5"
      Difficulty.MEDIUM -> "1 - 20"
      Difficulty.HARD -> "1 - 100"
    }
    AppLanguage.ENGLISH -> when (diff) {
      Difficulty.EASY -> "1 - 5"
      Difficulty.MEDIUM -> "1 - 20"
      Difficulty.HARD -> "1 - 100"
    }
  }

  fun getLedgeInstruction(lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> "جر الرقم أولا ورّك عليه 👆"
    AppLanguage.FRENCH -> "Glisse ou touche le chiffre 👆"
    AppLanguage.ENGLISH -> "Drag or tap the number 👆"
  }

  fun getVisualHelperActive(lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> "💡 معاون بصري نشط"
    AppLanguage.FRENCH -> "💡 Aide visuelle active"
    AppLanguage.ENGLISH -> "💡 Visual Helper Active"
  }

  fun getVisualHelperShow(lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> "💡 وريني الصور"
    AppLanguage.FRENCH -> "💡 Afficher l'aide"
    AppLanguage.ENGLISH -> "💡 Show Helper"
  }

  fun getLanguageDialogTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> "اختار اللغة ديالك 🌍"
    AppLanguage.FRENCH -> "Choisis ta langue 🌍"
    AppLanguage.ENGLISH -> "Choose your language 🌍"
  }

  fun getRandomPraise(lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> listOf(
      "تبارك الله عليك يا بطل! 🌟",
      "واعر بزاف! إجابة صحيحة! 🎯",
      "ممتاز يا فنان! 🚀",
      "برافو عليك! تبارك الله! 👑"
    ).random()
    AppLanguage.FRENCH -> listOf(
      "Bravo champion ! 🌟",
      "Super travail ! C'est exact ! 🎯",
      "Excellent, tu es un génie ! 🚀",
      "Magnifique réponse ! 👑"
    ).random()
    AppLanguage.ENGLISH -> listOf(
      "Awesome job, superstar! 🌟",
      "Spot on! Correct answer! 🎯",
      "Brilliant, math genius! 🚀",
      "Keep shining, champion! 👑"
    ).random()
  }

  fun getRandomEncouragement(lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> listOf(
      "غير بشوية، عاود حاول مرة خرى! 💪",
      "قربتي بزاف، جر الرقم الصحيح! 🎯",
      "ماشي مشكل، نتا بطل وعاود جرب! ⭐"
    ).random()
    AppLanguage.FRENCH -> listOf(
      "Réessaie encore, tu peux le faire ! 💪",
      "Presque ! Essaie encore un chiffre ! 🎯",
      "Pas de souci, continue champion ! ⭐"
    ).random()
    AppLanguage.ENGLISH -> listOf(
      "Try again, you can do it! 💪",
      "Almost there! Try another number! 🎯",
      "Don't give up, champion! ⭐"
    ).random()
  }

  fun getFruitName(fruit: TangibleItemType, lang: AppLanguage): String = when (lang) {
    AppLanguage.MOROCCAN_ARABIC -> when (fruit) {
      TangibleItemType.SHINY_APPLE -> "تفاحات حمرين"
      TangibleItemType.SWEET_ORANGE -> "ليمونات زوينين"
      TangibleItemType.YELLOW_BANANA -> "بنانات صفرين"
      TangibleItemType.RED_STRAWBERRY -> "فريزات حمرين"
      TangibleItemType.PURPLE_GRAPES -> "عرايش ديال العنب"
      TangibleItemType.JUICY_WATERMELON -> "طراف ديال الدلاح"
      else -> "تفاحات حمرين"
    }
    AppLanguage.FRENCH -> when (fruit) {
      TangibleItemType.SHINY_APPLE -> "pommes rouges"
      TangibleItemType.SWEET_ORANGE -> "oranges juteuses"
      TangibleItemType.YELLOW_BANANA -> "bananes jaunes"
      TangibleItemType.RED_STRAWBERRY -> "fraises sucrées"
      TangibleItemType.PURPLE_GRAPES -> "grappes de raisin"
      TangibleItemType.JUICY_WATERMELON -> "tranches de pastèque"
      else -> "pommes"
    }
    AppLanguage.ENGLISH -> when (fruit) {
      TangibleItemType.SHINY_APPLE -> "red apples"
      TangibleItemType.SWEET_ORANGE -> "sweet oranges"
      TangibleItemType.YELLOW_BANANA -> "yellow bananas"
      TangibleItemType.RED_STRAWBERRY -> "tasty strawberries"
      TangibleItemType.PURPLE_GRAPES -> "bunches of grapes"
      TangibleItemType.JUICY_WATERMELON -> "slices of watermelon"
      else -> "apples"
    }
  }

  fun generateStoryPrompt(
    op: MathOp,
    a: Int,
    b: Int,
    fruit: TangibleItemType,
    lang: AppLanguage
  ): String {
    val fruitName = getFruitName(fruit, lang)
    return when (lang) {
      AppLanguage.MOROCCAN_ARABIC -> when (op) {
        MathOp.PLUS -> "عندنا $a ديال $fruitName، وزدنا عليهم $b.. شحال ولا المجموع دابا؟"
        MathOp.MINUS -> "كان عندنا فالسلة $a ديال $fruitName، خدينا منهم $b.. شحال بقى دابا؟"
        MathOp.MULTIPLY -> "$a ديال المجموعات، فكل وحدة $b ديال $fruitName.. شحال عندنا كاملين؟"
        MathOp.DIVIDE, MathOp.MIXED -> "فرقنا $a ديال $fruitName بالتساوي على $b دراري.. شحال ياخد كل واحد؟"
      }
      AppLanguage.FRENCH -> when (op) {
        MathOp.PLUS -> "Nous avons $a $fruitName, et nous en ajoutons $b.. Combien au total ?"
        MathOp.MINUS -> "Il y a $a $fruitName dans le panier, on en prend $b.. Combien en reste-t-il ?"
        MathOp.MULTIPLY -> "$a groupes avec chacun $b $fruitName.. Combien au total ?"
        MathOp.DIVIDE, MathOp.MIXED -> "On partage $a $fruitName équitablement entre $b amis.. Combien chacun reçoit ?"
      }
      AppLanguage.ENGLISH -> when (op) {
        MathOp.PLUS -> "We have $a $fruitName, and we add $b more.. How many in total?"
        MathOp.MINUS -> "There were $a $fruitName in the basket, we took $b.. How many are left?"
        MathOp.MULTIPLY -> "$a groups of $b $fruitName.. How many in total?"
        MathOp.DIVIDE, MathOp.MIXED -> "Share $a $fruitName equally among $b friends.. How many each?"
      }
    }
  }
}
