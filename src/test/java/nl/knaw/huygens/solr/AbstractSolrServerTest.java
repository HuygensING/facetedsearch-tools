package nl.knaw.huygens.solr;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.slf4j.Logger;

import com.google.common.collect.Lists;

public class AbstractSolrServerTest {
  private static final int SECONDS_TO_COMMIT = 10;
  private static final int TIME_TO_COMMIT = 10000;
  private AbstractSolrServer instance;
  private SolrServer solrServer;
  private Logger logger;

  @Before
  public void setUp() {
    solrServer = mock(SolrServer.class);
    logger = mock(Logger.class);

    instance = new AbstractSolrServer(SECONDS_TO_COMMIT, null) {

      @Override
      protected SolrServer getSolrServer() {
        return solrServer;
      }

      @Override
      protected Logger getLogger() {
        return logger;
      }
    };
  }

  @Test
  public void testAdd() throws SolrServerException, IOException {
    SolrInputDocument doc = new SolrInputDocument();

    instance.add(doc);

    verify(solrServer).add(doc, TIME_TO_COMMIT);
  }

  @Test(expected = SolrServerException.class)
  public void testAddSolrServerException() throws SolrServerException, IOException {
    SolrInputDocument doc = new SolrInputDocument();
    doThrow(SolrServerException.class).when(solrServer).add(doc, TIME_TO_COMMIT);

    try {
      instance.add(doc);
    } finally {
      verify(solrServer).add(doc, TIME_TO_COMMIT);
    }
  }

  @Test(expected = IOException.class)
  public void testAddIOException() throws SolrServerException, IOException {
    SolrInputDocument doc = new SolrInputDocument();
    doThrow(IOException.class).when(solrServer).add(doc, TIME_TO_COMMIT);

    try {
      instance.add(doc);
    } finally {
      verify(solrServer).add(doc, TIME_TO_COMMIT);
    }
  }

  @Test
  public void testAddMultiple() throws SolrServerException, IOException {

    List<SolrInputDocument> docs = Lists.newArrayList(new SolrInputDocument(), new SolrInputDocument());

    instance.add(docs);

    verify(solrServer).add(docs, TIME_TO_COMMIT);
  }

  @Test(expected = SolrServerException.class)
  public void testAddMultipleSolrServerException() throws SolrServerException, IOException {
    List<SolrInputDocument> docs = Lists.newArrayList(new SolrInputDocument(), new SolrInputDocument());
    doThrow(SolrServerException.class).when(solrServer).add(docs, TIME_TO_COMMIT);

    try {
      instance.add(docs);
    } finally {
      verify(solrServer).add(docs, TIME_TO_COMMIT);
    }
  }

  @Test(expected = IOException.class)
  public void testAddMultipleIOException() throws SolrServerException, IOException {
    List<SolrInputDocument> docs = Lists.newArrayList(new SolrInputDocument(), new SolrInputDocument());
    doThrow(IOException.class).when(solrServer).add(docs, TIME_TO_COMMIT);

    try {
      instance.add(docs);
    } finally {
      verify(solrServer).add(docs, TIME_TO_COMMIT);
    }
  }

  @Test
  public void testCommit() throws SolrServerException, IOException {
    instance.commit();

    verify(solrServer).commit();
  }

  @Test(expected = SolrServerException.class)
  public void testCommitSolrServerException() throws SolrServerException, IOException {
    doThrow(SolrServerException.class).when(solrServer).commit();

    try {
      instance.commit();
    } finally {
      verify(solrServer).commit();
    }
  }

  @Test(expected = IOException.class)
  public void testCommitIOException() throws SolrServerException, IOException {
    doThrow(IOException.class).when(solrServer).commit();

    try {
      instance.commit();
    } finally {
      verify(solrServer).commit();
    }
  }

  @Test
  public void testDeleteById() throws SolrServerException, IOException {
    String id = "id";
    instance.deleteById(id);

    verify(solrServer).deleteById(id, TIME_TO_COMMIT);
  }

  @Test(expected = SolrServerException.class)
  public void testDeleteByIdSolrServerException() throws SolrServerException, IOException {
    String id = "id";
    doThrow(SolrServerException.class).when(solrServer).deleteById(id, TIME_TO_COMMIT);

    try {
      instance.deleteById(id);
    } finally {
      verify(solrServer).deleteById(id, TIME_TO_COMMIT);
    }
  }

  @Test(expected = IOException.class)
  public void testDeleteByIdIOException() throws SolrServerException, IOException {
    String id = "id";
    doThrow(IOException.class).when(solrServer).deleteById(id, TIME_TO_COMMIT);

    try {
      instance.deleteById(id);
    } finally {
      verify(solrServer).deleteById(id, TIME_TO_COMMIT);
    }
  }

  @Test
  public void testDeleteByIdMultiple() throws SolrServerException, IOException {
    List<String> ids = Lists.newArrayList("id1", "id2", "id3");
    instance.deleteById(ids);

    verify(solrServer).deleteById(ids, TIME_TO_COMMIT);
  }

  @Test(expected = SolrServerException.class)
  public void testDeleteByIdMultipleSolrServerException() throws SolrServerException, IOException {
    List<String> ids = Lists.newArrayList("id1", "id2", "id3");
    doThrow(SolrServerException.class).when(solrServer).deleteById(ids, TIME_TO_COMMIT);

    try {
      instance.deleteById(ids);
    } finally {
      verify(solrServer).deleteById(ids, TIME_TO_COMMIT);
    }
  }

  @Test(expected = IOException.class)
  public void testDeleteByIdMultitpleIOException() throws SolrServerException, IOException {
    List<String> ids = Lists.newArrayList("id1", "id2", "id3");
    doThrow(IOException.class).when(solrServer).deleteById(ids, TIME_TO_COMMIT);

    try {
      instance.deleteById(ids);
    } finally {
      verify(solrServer).deleteById(ids, TIME_TO_COMMIT);
    }
  }

  @Test
  public void testDeleteByQuery() throws SolrServerException, IOException {
    String query = "test:test";

    instance.deleteByQuery(query);

    verify(solrServer).deleteByQuery(query, TIME_TO_COMMIT);

  }

  @Test(expected = SolrServerException.class)
  public void testDeleteByQuerySolrServerException() throws SolrServerException, IOException {
    String query = "test:test";
    doThrow(SolrServerException.class).when(solrServer).deleteByQuery(query, TIME_TO_COMMIT);

    try {
      instance.deleteByQuery(query);
    } finally {
      verify(solrServer).deleteByQuery(query, TIME_TO_COMMIT);
    }
  }

  @Test(expected = IOException.class)
  public void testDeleteByQueryIOException() throws SolrServerException, IOException {
    String query = "test:test";
    doThrow(IOException.class).when(solrServer).deleteByQuery(query, TIME_TO_COMMIT);

    try {
      instance.deleteByQuery(query);
    } finally {
      verify(solrServer).deleteByQuery(query, TIME_TO_COMMIT);
    }
  }

  @Test
  public void testEmpty() throws SolrServerException, IOException {
    instance.empty();

    verify(solrServer).deleteByQuery("*:*", TIME_TO_COMMIT);
  }

  @Test(expected = SolrServerException.class)
  public void testEmptySolrServerException() throws SolrServerException, IOException {
    doThrow(SolrServerException.class).when(solrServer).deleteByQuery("*:*", TIME_TO_COMMIT);

    try {
      instance.empty();
    } finally {
      verify(solrServer).deleteByQuery("*:*", TIME_TO_COMMIT);
    }
  }

  @Test(expected = IOException.class)
  public void testEmptyIOException() throws SolrServerException, IOException {
    doThrow(IOException.class).when(solrServer).deleteByQuery("*:*", TIME_TO_COMMIT);

    try {
      instance.empty();
    } finally {
      verify(solrServer).deleteByQuery("*:*", TIME_TO_COMMIT);
    }
  }

  @Test
  public void testPing() throws SolrServerException, IOException {

    instance.ping();

    verify(solrServer).ping();

  }

  @Test
  public void testPingSolrException() throws SolrServerException, IOException {
    doThrow(SolrServerException.class).when(solrServer).ping();

    instance.ping();

    InOrder inOrder = inOrder(solrServer, logger);
    inOrder.verify(solrServer).ping();
    inOrder.verify(logger).error(anyString(), anyString());

  }

  @Test
  public void testPingIOException() throws SolrServerException, IOException {
    doThrow(IOException.class).when(solrServer).ping();

    instance.ping();

    InOrder inOrder = inOrder(solrServer, logger);
    inOrder.verify(solrServer).ping();
    inOrder.verify(logger).error(anyString(), anyString());
  }

  @Test
  public void testCommitAndOptimize() throws SolrServerException, IOException {
    instance.commitAndOptimize();

    InOrder inOrder = inOrder(solrServer);

    inOrder.verify(solrServer).commit();
    inOrder.verify(solrServer).optimize();
  }

  @Test(expected = SolrServerException.class)
  public void testCommitAndOptimizeSolrServerExceptionCommit() throws SolrServerException, IOException {
    doThrow(SolrServerException.class).when(solrServer).commit();

    try {
      instance.commitAndOptimize();
    } finally {
      verify(solrServer).commit();
      verify(solrServer, never()).optimize();
    }
  }

  @Test(expected = IOException.class)
  public void testCommitAndOptimizeIOExceptionCommit() throws SolrServerException, IOException {
    doThrow(IOException.class).when(solrServer).commit();

    try {
      instance.commitAndOptimize();
    } finally {
      verify(solrServer).commit();
      verify(solrServer, never()).optimize();
    }
  }

  @Test(expected = SolrServerException.class)
  public void testCommitAndOptimizeSolrServerExceptionOptimize() throws SolrServerException, IOException {
    doThrow(SolrServerException.class).when(solrServer).optimize();

    try {
      instance.commitAndOptimize();
    } finally {
      verify(solrServer).commit();
      verify(solrServer).optimize();
    }
  }

  @Test(expected = IOException.class)
  public void testCommitAndOptimizeIOExceptionCommitOptimize() throws SolrServerException, IOException {
    doThrow(IOException.class).when(solrServer).optimize();

    try {
      instance.commitAndOptimize();
    } finally {
      InOrder inOrder = inOrder(solrServer);
      inOrder.verify(solrServer).commit();
      inOrder.verify(solrServer).optimize();
    }
  }

  @Test
  public void testSearch() throws SolrServerException {
    SolrQuery queryMock = mock(SolrQuery.class);

    instance.search(queryMock);

    verify(solrServer).query(queryMock);
  }

  @Test(expected = SolrServerException.class)
  public void testSearchSolrServerException() throws SolrServerException {
    doThrow(SolrServerException.class).when(solrServer).query(any(SolrQuery.class));

    SolrQuery queryMock = mock(SolrQuery.class);

    try {
      instance.search(queryMock);
    } finally {

      verify(solrServer).query(any(SolrQuery.class));
    }
  }

  @Test
  public void testShutdown() throws SolrServerException, IOException {

    instance.shutdown();

    InOrder inOrder = inOrder(solrServer);
    inOrder.verify(solrServer).commit();
    inOrder.verify(solrServer).optimize();

  }

  @Test(expected = SolrServerException.class)
  public void testShutdownSolrException() throws SolrServerException, IOException {
    doThrow(SolrServerException.class).when(solrServer).commit();
    instance.shutdown();

    InOrder inOrder = inOrder(solrServer, logger);
    inOrder.verify(solrServer).commit();
    inOrder.verify(logger).error(anyString(), anyString());
  }

  @Test(expected = IOException.class)
  public void testShutdownIOException() throws SolrServerException, IOException {
    doThrow(IOException.class).when(solrServer).commit();
    instance.shutdown();

    InOrder inOrder = inOrder(solrServer, logger);
    inOrder.verify(solrServer).commit();
    inOrder.verify(logger).error(anyString(), anyString());
  }

}
