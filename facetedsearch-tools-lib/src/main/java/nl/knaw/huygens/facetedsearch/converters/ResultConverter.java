package nl.knaw.huygens.facetedsearch.converters;

import nl.knaw.huygens.facetedsearch.model.FacetedSearchResult;

import org.apache.solr.client.solrj.response.QueryResponse;

public class ResultConverter implements QueryResponseConverter {

  private ResultMetaDataConverter resultMetaDataConverter;
  private RawResultConverter rawResultConverter;

  public ResultConverter(ResultMetaDataConverter resultMetaDataConverter, RawResultConverter rawResultConverter) {
    this.resultMetaDataConverter = resultMetaDataConverter;
    this.rawResultConverter = rawResultConverter;
  }

  public ResultConverter() {
    this(new ResultMetaDataConverter(), new RawResultConverter());
  }

  @Override
  public void convert(final FacetedSearchResult result, final QueryResponse queryResponse) {
    rawResultConverter.convert(result, queryResponse);
    resultMetaDataConverter.convert(result, queryResponse);
  }
}
