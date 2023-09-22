class WordleAnswer {

    private final char notIn; // the character for black 
    private final char otherPlace; // the character for yellow 
    private final char rightPlace; // the character for green
    private final int ANSWER_LENGTH = 5;
    private String answer = null;
    private int knownLettersCount = 0;
    private int isThere = 5;
    private int notThere = 6;
    private int unknown = -1;
    private char[] answerCharacters = new char[5];
    private int[] characters = new int[26];
    private int[][] yellowPlaces = new int[26][4];
    private char[] mustContain = new char[5];

    public char getNotIn() {
        return notIn;
    }

    public char getOtherPlace() {
        return otherPlace;
    }

    public char getRightPlace() {
        return rightPlace;
    }

    public WordleAnswer(char black, char yellow, char green) {
        this.notIn = black;
        this.otherPlace = yellow;
        this.rightPlace = green;
        for(int i = 0; i < characters.length; i++) {
            characters[i] = unknown;
            for (int j = 0; j < 4; j++) {
                yellowPlaces[i][j] = unknown;
            }
        }
    }

    //no parameter constructor with default values
    public WordleAnswer() {
        this('0', '1', '2');
    }

    public boolean canBe(String word) {
        int counter = 0;
        boolean canBe = true;

        for (int num : characters) {    // checks if all the yellow & green letters exist in word
            if (num == isThere && !exist(word.toCharArray(), (char) ('a' + counter))) {
                canBe = false;
                break;
            }
            else if (num > unknown && num < isThere && !exist(word.toCharArray(), (char) ('a' + counter))) {
                canBe = false;
                break;
            }
            counter ++;
        }

        for (int i = 0; i < 5; i++) {
            if (!canHaveLetterAtIndex(word.charAt(i), i)) {
                canBe = false;
                break;
            }
        }
        return canBe;
    }

    public void addGuess(String word, String guessResult) {
        int k = 0;
        for (int i = 0; i < ANSWER_LENGTH; i++) {

            if (guessResult.charAt(i) == notIn) {
                characters[word.charAt(i)- 'a'] = notThere;
            }
            if (guessResult.charAt(i) == otherPlace) {
                if (!(characters[word.charAt(i)- 'a'] > unknown && characters[word.charAt(i)- 'a'] < isThere)) {
                    characters[word.charAt(i)- 'a'] = isThere;
                }
                for (int j = 0; j < 4; j++) {
                    if (yellowPlaces[word.charAt(i) - 'a'][j] == unknown) {
                        yellowPlaces[word.charAt(i) - 'a'][j] = i;
                        break;
                    }
                }
            }
            if (guessResult.charAt(i) == rightPlace) {
                characters[word.charAt(i)- 'a'] = i;
            }
        }

        if (guessResult.equals("22222") && answer == null) {
            answer = word;
        }

        for (int i = 0; i < 26; i++) {
            if (characters[i] > -1 && characters[i] < 5) {
                answerCharacters[characters[i]] = (char) ('a' + i);
            }
            if ( characters[i] != unknown) {
                mustContain[k] = (char) ('a' + i);
            }
        }
    }

    public boolean canContain(char letter) {
        boolean exist = true;

        if (characters[(letter - 'a')] == notThere) {
            exist = false;      // black letter
        }

        if (getKnownLettersCount() == 5 && characters[(letter - 'a')] == unknown) {
            exist = false;
        }
        return exist;
    }

    public boolean canHaveLetterAtIndex(char letter, int index) {
        boolean exist = true;
        if (characters[(letter - 'a')] == notThere){
            exist = false;          // black letter
        }
        else if ((characters[(letter - 'a')] > unknown && characters[(letter - 'a')] < isThere) && answerCharacters[index] != letter ) {
            exist = false;          // green letter, different index
        }
        else if (characters[(letter - 'a')] == unknown && (answerCharacters[index] != '\0' || full(answerCharacters))) {
            exist = false;          // unknown or answerCharacters already full (answer found)
        }
        if (getKnownLettersCount() == 5 && characters[(letter - 'a')] == unknown) {      // unknown input but known yellow and green letters = 5
            exist = false;
        }
        for (int i = 0; i < 4; i++) {
            if (yellowPlaces[letter - 'a'][i] != unknown && yellowPlaces[letter - 'a'][i] == index) {
                exist = false;
                break;
            }
        }
        if (answerCharacters[index] != '\0' && answerCharacters[index] != letter) {
            exist = false;
        }
        return exist;
    }

    public int getKnownLettersCount() {
        knownLettersCount = 0;
        for (int info : characters) {
            if (info != unknown && info != notThere) {
                knownLettersCount++;
            }
        }
        return knownLettersCount;
    }

    public boolean isFound() {
        return answer != null;
    }

    public String getAnswer() {
        return answer;
    }

    private boolean full(char[] array) {
        boolean full = true;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '\0') {
                full = false;
                break;
            }
        }
        return full;
    }
 
    private boolean exist(char[] array, char letter) {
        boolean exist = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == letter) {
                exist = true;
                break;
            }
        }
        return exist;
    }
}
