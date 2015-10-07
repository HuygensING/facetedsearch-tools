package nl.knaw.huygens.facetedsearch.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.*;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.parser.SolrQueryParserBase;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class SolrUtils {
  // Special Lucene characters:
  //this regex matches on
  // - possibly: an even number of escapes (indicating a sequence of escaped backslashes)
  // - followed by a lucene character: + - & | ! ( ) { } [ ] ^ " ~ * ? : \
  //so + will match, \+ will not, \\+ will, \\\+ won't etc.
  private static final Pattern SPECIAL = Pattern.compile("((?<!\\\\)(\\\\\\\\)*)[-+&|!(){}\\[\\]\\^\"~*?:]");

  private SolrUtils() {}

  /**
   * Adds fuzzy modifiers to plain text tokens. Will not do anything if it encounters a string that is considered
   * 'advanced' i.e. a string that contains solr modifiers.
   * @param fullTextSearchValue
   * @return
   */
  public static String fuzzy(String fullTextSearchValue) {
    if (StringUtils.isBlank(fullTextSearchValue) || hasUnescapedSolrSpecialCharacters(fullTextSearchValue)) {
      return fullTextSearchValue;
    } else {
      return Arrays.stream(StringUtils.split(fullTextSearchValue))
        //escaping is not needed because we are only in the else branch if no special character appears in the string
        //.map(x -> SolrQueryParserBase.escape(x))
        .map(x -> x.length() < 4 ? x + "~0.5" : x + "~0.7")
        .collect(Collectors.joining(" AND "));
    }
  }

  public static boolean hasUnescapedSolrSpecialCharacters(String str) {
    return SPECIAL.matcher(str).find();
  }

  public static String escapeFulltextValue(String fullTextSearchValue, boolean isFuzzySearch) {
    String cleaned = SolrQueryParserBase.escape(fullTextSearchValue);
    if (isFuzzySearch) {
      cleaned = fuzzy(cleaned);
    }
    cleaned = cleaned.trim();

    if (cleaned.contains(" ")) {
      return String.format("(%s)", cleaned);
    } else {
      return cleaned;
    }
  }

  public static String escapeFacetValue(String string) {
    return SolrQueryParserBase.escape(string).replaceAll(" ", "\\\\ ");
  }

  public static List<String> splitTerms(String terms) {
    Iterable<String> split = Splitter.on(" ").split(terms);
    StringBuilder tmpTerm = null;
    boolean append = false;
    List<String> termlist = Lists.newArrayList();
    for (String part : split) {
      if (part.startsWith("\"")) {
        tmpTerm = new StringBuilder(part);
        append = true;

      } else if (part.endsWith("\"")) {
        tmpTerm.append(" ").append(part);
        termlist.add(tmpTerm.toString());
        append = false;

      } else if (append) {
        tmpTerm.append(" ").append(part);

      } else {
        termlist.add(part);
      }
    }
    return termlist;
  }

}
