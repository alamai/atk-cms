package atk.cms.tests;

import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Represents a SINGLE Question with: 
 * (1) Question number
 * (2) Question statement
 * (3) Question choices, with 4 possible answers
 * (4) Correct choice index
 */
@ManagedBean(name="question")
@SessionScoped
public class Question implements Serializable {

	private ArrayList<String> answers = new ArrayList<String>(10);
	private boolean answered = false;
	private String question;
	private int questionNumber;
	private String[] questionChoices;
	private int correctChoiceIndex;
	private static final long serialVersionUID = 1L;
	
	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
		answered = true;
	}
	
	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswered(boolean answered) { 
		this.answered = answered; 
	}

	public boolean isAnswered() { 
		return answered; 
	}

	public void setQuestion (String question) { 
		this.question = question; 
	}

	public String getQuestion() { 
		return question; 
	}
	
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	
	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionChoices(String[] s) {
		questionChoices = s;
	}

	public String[] getQuestionChoices() { 
		return questionChoices;  
	}

	// Set Question i's correct answer from XML file
	public void setCorrectChoiceIndex(int i) {
		correctChoiceIndex = i;
	}
	
	// Get Question i's correct answer from XML file
	public int getCorrectChoiceIndex() {
		return correctChoiceIndex;
	}
}