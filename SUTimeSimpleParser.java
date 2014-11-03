import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;

import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Generics;


public class SUTimeSimpleParser {

  private SUTimeSimpleParser() {} // static methods

  public static class SUTimeParsingError extends Exception {
    private static final long serialVersionUID = 1L;
    public String timeExpression;

    public SUTimeParsingError(String timeExpression) {
      this.timeExpression = timeExpression;
    }

    public String getLocalizedMessage() {
      return "Error while parsing '" + timeExpression + "'";
    }

  }

  private static AnnotationPipeline pipeline;
  private static Map<String, Temporal> cache;
  public static int calls = 0;
  public static int misses = 0;

  static {
    pipeline = makeNumericPipeline();
    cache = Generics.newHashMap();
  }

  private static AnnotationPipeline makeNumericPipeline() {
    AnnotationPipeline pipeline = new AnnotationPipeline();
    pipeline.addAnnotator(new TokenizerAnnotator(false, "en"));
    pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
    pipeline.addAnnotator(new POSTaggerAnnotator(false));
    pipeline.addAnnotator(new TimeAnnotator(true));

    return pipeline;
  }


  public static void parse(String str) throws SUTimeParsingError {
    try {
      Annotation doc = new Annotation(str);
      pipeline.annotate(doc);

      assert doc.get(CoreAnnotations.SentencesAnnotation.class) != null;
      assert doc.get(CoreAnnotations.SentencesAnnotation.class).size() > 0;
      List<CoreMap> timexAnnotations = doc.get(TimeAnnotations.TimexAnnotations.class);
      int size=timexAnnotations.size();
      ArrayList<String> al=new ArrayList<String>();
      for(int i=0;i<size;i++)
      {
    	  CoreMap timex = timexAnnotations.get(i);
    	  Temporal ex=timex.get(TimeExpression.Annotation.class).getTemporal();
    	 System.out.println(ex);
      }
             
    } catch (Exception e) {
      SUTimeSimpleParser.SUTimeParsingError parsingError = new SUTimeSimpleParser.SUTimeParsingError(str);
      parsingError.initCause(e);
      throw parsingError;
    }
  }



  public static void main(String[] args) throws SUTimeParsingError {
    String s="The cultural team is borrowing the gym speakers for the day and would place it back by tomorrow. This the gym will not have speakers till tomorrow morning.";
      System.out.println("String: " + s);
      parse(s);
      System.out.println();
    
  }

}