package atk.cms.tests;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Keep track of current question presented to Student
 * Save Student's Question and Answer selections in a Map
 * Save questions answered in List and traverse Map to retrieve Student's answers
 * questionList stores list of Questions
 */
@ManagedBean(name="questions")
@SessionScoped
public class Questions implements Serializable {

	protected int currentQuestion = 0;
			
	// List for questions from XML file 
	protected ArrayList<Question> questionsList = new ArrayList<Question>(10);

	// List for Student's selected answers 
	protected Map<Integer, Integer> selections = new LinkedHashMap<Integer, Integer>();

	private final String testsPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator 
			+ "CMSFiles" + File.separator + "AssignedTests" + File.separator + "java-test.xml";
	
	private static final long serialVersionUID = 1L;

	public int getCurrentQuestion() { 
		return currentQuestion;
	}
	
	public void resetCurrentQuestion() { 
		currentQuestion = 0;
	}
	
	public ArrayList<Question> getQuestionsList() { 
		return this.questionsList;
	}

	public Map<Integer, Integer> getSelections() {
		return this.selections;
	}

	public Questions() {
	}

	/**
	 * Sets Question when Student clicks Next or Previous
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public void setQuestion(int i) throws SAXException, ParserConfigurationException, IOException, URISyntaxException {   

		int number = i;
		int correct = 0;
		int counter = 0;
		String question = null;
		String choices[] = new String[4];
		Document testDOM = createDOM();

		// Get list of all questions from XML file
		NodeList qList = testDOM.getElementsByTagName("question");

		// Get contents of each question - child nodes: question, answer, correct 
		NodeList childList = qList.item(i).getChildNodes();

		System.out.println("Total Test Questions " + testDOM.getElementsByTagName("totaltestquestions").item(0).getTextContent());
		System.out.println("Test Duration " + testDOM.getElementsByTagName("testduration").item(0).getTextContent());

		// Iterate through each question's child nodes
		for (int j = 0; j < childList.getLength(); j++) {
			Node childNode = childList.item(j);

			// Assign answer child nodes to answers (4 total)
			if ("answer".equals(childNode.getNodeName())) {
				choices[counter] = childList.item(j).getTextContent();
				counter++;
			}

			// Assign test question child node to question (1 total)
			else if ("testquestion".equals(childNode.getNodeName())) {
				question = childList.item(j).getTextContent();
			}

			// Assign correct child node to correct (1 total)
			else if ("correct".equals(childNode.getNodeName())) {
				correct = Integer.parseInt(childList.item(j).getTextContent());
			}
		}

		System.out.println("Retrieving Question Number " + number);
		System.out.println("Question is: " + question);

		for (String a : choices) {
			System.out.println(a);
		}
		System.out.println("Correct answer index : " + correct);
		
		Wizard wiz = new Wizard();
		System.out.println("Button ID is: " + wiz.getButton_id());
		
		Question qn = new Question();
		qn.setQuestionNumber(number);
		qn.setQuestion(question);
		qn.setQuestionChoices(choices);
		qn.setCorrectChoiceIndex(correct);
		questionsList.add(number, qn);
	}

	/**
	 * Calculates test score when Student clicks "Finish" or when timer expires
	 * Compares Student's answers with specified correct answers 
	 * @param questions
	 * @return correct answers
	 */
	public int calculateResult(Questions questions) {

		int totalCorrect = 0;

		Map<Integer, Integer> userSelectionsMap = questions.selections;	

		List<Integer> userSelectionsList = new ArrayList<Integer>(10);

		// Construct list of Student's answers
		for (Map.Entry<Integer, Integer> entry : userSelectionsMap.entrySet()) {
			userSelectionsList.add(entry.getValue());
		}

		List<Question> qList = questions.questionsList;

		List<Integer> correctAnswersList = new ArrayList<Integer>(10);

		// Construct list of instructor's correct answers
		for (Question question : qList) {
			correctAnswersList.add(question.getCorrectChoiceIndex());
		}

		for (int i = 0; i < selections.size(); i++) {
			System.out.println(userSelectionsList.get(i) + " - " + correctAnswersList.get(i));

			// Compare Student's to Instructor's answers. If correct, increment correct total
			if ((userSelectionsList.get(i) - 1) == correctAnswersList.get(i)) {
				totalCorrect++;
			}
		}
		System.out.println("Your correct-question score is " + totalCorrect);	
		return totalCorrect;
	}

	/**
	 * Create Document Object Model to store test questions
	 * DOM keeps a reference to a DOM object for selected test XML file
	 * @return DOM
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Document createDOM() throws SAXException, ParserConfigurationException,
	IOException, URISyntaxException {

		Document dom = null;
		File testFile = null;
		testFile = new File(testsPath);
		System.out.println("Test file path " + testFile.getAbsolutePath());


		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		try {
			dom = db.parse(testFile);
		} catch (FileNotFoundException noFileFound) {
			System.out.println(noFileFound);
		}
		dom.getDocumentElement().normalize();
		return dom;
	}
}