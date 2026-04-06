com.trivia.app/
├── model/
│   ├── entity/         Category.java, Question.java
│   ├── dao/            CategoryDao.java, QuestionDao.java
│   ├── database/       TriviaDatabase.java  (seed automático)
│   └── repository/     TriviaRepository.java
├── controller/
│   ├── HomeViewModel.java
│   └── QuizViewModel.java  (timer, score, estados)
└── view/
    ├── MainActivity.java       (RecyclerView de categorías)
    ├── QuizActivity.java       (contenedor de fragments + confirmación de salida)
    ├── ResultActivity.java     (puntaje + opciones de reinicio)
    ├── adapter/
    │   └── CategoryAdapter.java  (con DiffUtil + animación escalonada)
    └── fragment/
        └── QuestionFragment.java  (timer, opciones, animaciones de respuesta)
