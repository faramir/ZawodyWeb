package pl.umk.mat.zawodyweb.checker.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.checker.CheckerInterface;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.Program;
import pl.umk.mat.zawodyweb.database.CheckerErrors;

/**
 * QuizDiff Porównuje odpowiedzi testowe zawarte w jednym pliku z odpowiedziami,
 * które uczestnicy zapisali, np. 1. A 2. B 3. C vs 2) B 1) B 3. C
 *
 * @author faramir
 */
public class QuizDiff implements CheckerInterface {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(QuizDiff.class);
    private Pattern pattern = Pattern.compile("\\s*(\\w+)(?:\\s*\\W+|\\s+\\W*)\\s*(.*)\\s*");

    @Override
    public CheckerResult check(Program program, TestInput input, TestOutput output) {

        TestOutput codeOutput = program.runTest(input);
        if (codeOutput.getResult() != CheckerErrors.UNDEF) {
            return new CheckerResult(codeOutput.getResult(), codeOutput.getResultDesc());
        }
        CheckerResult result = new CheckerResult();

        String codeText = codeOutput.getText();
        String rightText = output.getText();
        double p = diff(codeText, rightText);

        int points = (int) Math.round(p * input.getMaxPoints());
        result.setPoints(points);

        if (points == 0) {
            result.setResult(CheckerErrors.WA);
        } else {
            result.setResult(CheckerErrors.ACC);
        }

        result.setRuntime(codeOutput.getRuntime());
        result.setMemUsed(codeOutput.getMemUsed());
        return result;
    }

    private Map<String, String> mapAnswers(String text) {
        Map<String, String> map = new HashMap<String, String>();
        for (String line : text.split("[\n\r]")) {
            Matcher matcher = pattern.matcher(line.trim());
            if (matcher.matches()) {
                map.put(matcher.group(1).trim().toLowerCase(), matcher.group(2).trim().toLowerCase());
            }
        }
        return map;
    }

    private double diff(String userText, String rightText) {
        if (userText == null || rightText == null) {
            return 0.0;
        }
        try {
            Map<String, String> right = mapAnswers(rightText);
            Map<String, String> user = mapAnswers(userText);
            int questions = 0;
            int correct = 0;
            for (Map.Entry<String, String> question : right.entrySet()) {
                ++questions;
                if (user.get(question.getKey()) == null) {
                    continue;
                }
                if (user.get(question.getKey()).equals(question.getValue())) {
                    ++correct;
                }
            }
            return (double) correct / questions;
        } catch (Exception ex) {
            logger.debug("Quiz error: ", ex);
        }
        return 0.0;
    }
//    public static void main(String[] args) {
//        System.out.println(new QuizDiff().diff("1 Marek\n2.Nowicki", "1. Marek\n2)Nowicki"));
//    }
}
