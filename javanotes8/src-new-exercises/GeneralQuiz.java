/**
 * This program creates, administers, and grades a quiz made up of ten questions,
 * where each question has an integer answer.  The quiz includes some simple addition
 * problems, some subtraction problems, and some non-math questions.
 */
public class GeneralQuiz {

    // -------------------- Nested classes and interface -----------------------

    interface IntQuestion {
        public String getQuestion();
        public int getCorrectAnswer();
    }
    
    static class AdditionQuestion implements IntQuestion {
       private int a, b;  // The numbers in the problem.
       public AdditionQuestion() { // constructor
           a = (int)(Math.random() * 50 + 1);
           b = (int)(Math.random() * 50);
       }
       public String getQuestion() {
           return "What is " + a + " + " + b + " ?";
       }
       public int getCorrectAnswer() {
           return a + b;
       }
    }

    static class SubtractionQuestion implements IntQuestion {
       private int a, b;  // The numbers in the problem.
       public SubtractionQuestion() { // constructor
           a = (int)(Math.random() * 50 + 1);
           b = (int)(Math.random() * 50);
           if (b > a) { // swap a and b so answer won't be negative
              int temp = a;
              a = b;
              b = temp;
           }
       }
       public String getQuestion() {
           return "What is " + a + " - " + b + " ?";
       }
       public int getCorrectAnswer() {
           return a - b;
       }
    }
    
    // -------------------- The Program --------------------------------------

    private static IntQuestion[] questions;  // The questions for the quiz

    private static int[] userAnswers;   // The user's answers to the ten questions.
    
    
    public static void main(String[] args) {
        System.out.println();
        System.out.println("Welcome to the quiz");
        System.out.println();
        System.out.println("There are some math questions and a few non-math");
        System.out.println("questions, but the answer to every question is");
        System.out.println("an integer.");
        System.out.println();
        createQuiz();
        administerQuiz();
        gradeQuiz();
    }
    
    
    /**
     * Creates the array of objects that holds the quiz questions
     */
    private static void createQuiz() {
        questions = new IntQuestion[10];
        for ( int i = 0; i < 7; i++ ) {
            if (Math.random() < 0.5)
	            questions[i] = new AdditionQuestion();
	        else
	            questions[i] = new SubtractionQuestion();
        }
        questions[7] = new IntQuestion() {
              public String getQuestion() {
                  return "How many states are there in the United States?";
              }
              public int getCorrectAnswer() {
                  return 50;
              }
        };
        questions[8] = new IntQuestion() {
              public String getQuestion() {
                  return "In what year did the First World War begin?";
              }
              public int getCorrectAnswer() {
                  return 1914;
              }
        };
        questions[9] = new IntQuestion() {
              public String getQuestion() {
                  return "What is the answer to the ultimate question " +
                                "of life, the universe, and everything?";
              }
              public int getCorrectAnswer() {
                  return 42;
              }
        };
   }        
    
    
    /**
     * Asks the user each of the ten quiz questions and gets the user's answers.
     * The answers are stored in an array, which is created in this subroutine.
     */
    private static void administerQuiz() {
        userAnswers = new int[10];
        for (int i = 0; i < 10; i++) {
            int questionNum = i + 1;
            System.out.printf("Question %2d:  %s ",
                                  questionNum, questions[i].getQuestion());
            userAnswers[i] = TextIO.getlnInt();
        }
    }
    
    
    /**
     * Shows all the questions, with their correct answers, and computes a grade
     * for the quiz.  For each question, the user is told whether they got
     * it right.
     */
    private static void gradeQuiz() {
        System.out.println();
        System.out.println("Here are the correct answers:");
        int numberCorrect = 0;
        int grade;
        for (int i = 0; i < 10; i++) {
            System.out.println("Question number " + (i+1) + ":");
            System.out.println("    " + questions[i].getQuestion());
            System.out.println("    Correct answer:  " + questions[i].getCorrectAnswer());
            if ( userAnswers[i] == questions[i].getCorrectAnswer() ) {
                System.out.println("    You were CORRECT.");
                numberCorrect++;
            }
            else {
                System.out.println("    You said " + userAnswers[i] + ", which is INCORRECT.");
            }
        }
        grade = numberCorrect * 10;
        System.out.println();
        System.out.println("You got " + numberCorrect + " questions correct.");
        System.out.println("Your grade on the quiz is " + grade);
        System.out.println();
    }

} // end class GeneralQuiz