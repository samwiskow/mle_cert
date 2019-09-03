package mle.cert.assignment.fe;

import com.workfusion.vds.sdk.api.nlp.annotation.OnDocumentStart;
import com.workfusion.vds.sdk.api.nlp.fe.Feature;
import com.workfusion.vds.sdk.api.nlp.fe.FeatureExtractor;
import com.workfusion.vds.sdk.api.nlp.model.Cell;
import com.workfusion.vds.sdk.api.nlp.model.Document;
import com.workfusion.vds.sdk.api.nlp.model.Element;
import com.workfusion.vds.sdk.api.nlp.model.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Create a feature for every value in a column\row where appropriate column\row header contains any of keyword list provided.
 *
 * @param <T>
 */
public class MatchFullColumnOrRowWithSpecifiedHeaderFeatureExtractror<T extends Element> implements FeatureExtractor<T> {

    private String FEATURE_NAME;
    private Set<String> HEADER_KEYWORDS;
    private Map<Integer, Cell> HEADERS_MAPPING = new TreeMap<>();
    private boolean IS_USED_FOR_COLUMN;

    public MatchFullColumnOrRowWithSpecifiedHeaderFeatureExtractror(String featureName, Set<String> columnNames, boolean isUsedForColumn) {
        if (isUsedForColumn) {
            FEATURE_NAME = featureName + "_match_table_column_header";
        } else {
            FEATURE_NAME = featureName + "_match_table_row_header";
        }
        IS_USED_FOR_COLUMN = isUsedForColumn;
        HEADER_KEYWORDS = columnNames;
    }

    /**
     * Code is running on document load one time per document.
     *
     * @param document
     */
    @OnDocumentStart
    public void OnDocumentStart(Document document) {
        Collection<Table> coveringTable = document.findAll(Table.class);
        for (Table table : coveringTable) {
            List<Cell> coveredCells = document.findCovered(Cell.class, table);
            for (Cell coveredCell : coveredCells) {
                for (String columnName : HEADER_KEYWORDS) {
                    if (coveredCell.getText().toLowerCase().contains(columnName)) {
                        HEADERS_MAPPING.put(table.hashCode(), coveredCell);
                    }
                }
            }
        }
    }

    @Override
    public Collection<Feature> extract(Document document, T element) {
        List<Feature> list = new ArrayList<>();

        List<Cell> coveringCell = document.findCovering(Cell.class, element);
        List<Table> coveringTable = document.findCovering(Table.class, element);
        if (coveringTable.size() > 0 && HEADERS_MAPPING.size() > 0) {
            Integer key = coveringTable.get(0).hashCode();
            for (Cell cell : coveringCell) {
                if (HEADERS_MAPPING.containsKey(key)) {
                    Cell headerCell = HEADERS_MAPPING.get(key);
                    if (IS_USED_FOR_COLUMN) {
                        if (headerCell.getColumnIndex() == cell.getColumnIndex() && headerCell.getRowIndex() < cell.getRowIndex()) {
                            list.add(new Feature(FEATURE_NAME, 1.0));
                        }
                    } else {
                        if (headerCell.getRowIndex() == cell.getRowIndex() && headerCell.getColumnIndex() < cell.getColumnIndex()) {
                            list.add(new Feature(FEATURE_NAME, 1.0));
                        }
                    }
                }
            }
        }
        return list;
    }
}
