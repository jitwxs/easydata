package com.github.jitwxs.addax.common.util;

import com.github.jitwxs.addax.conn.MySQLConnection;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author jitwxs@foxmail.com
 * @since 2022-03-19 19:51
 */
public class LoadingUtils {
    private static final Class<?> CLAZZ = LoadingUtils.class;

    /**
     * load csv content
     *
     * @param path file path
     * @return file content with pair <title_line, data_lines>
     */
    public static List<String[]> loadCsv(final String path) throws IOException, CsvException {
        try (final InputStream inputStream = CLAZZ.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("InputStream Not Exist");
            }

            try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
                return reader.readAll();
            }
        }
    }

    /**
     * load json content
     *
     * @param path file path
     */
    public static List<String> loadJson(final String path, final Charset charset) throws IOException {
        try (final InputStream inputStream = CLAZZ.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("InputStream Not Exist");
            }

            final String string = IOUtils.toString(inputStream, charset);

            // single one
            if (string.startsWith("{")) {
                return Collections.singletonList(string);
            }

            final List<String> resultList = new ArrayList<>();
            final char[] chars = string.toCharArray();
            final StringBuilder sb = new StringBuilder();

            int brace = 0;
            boolean isIncremented = false;

            for (final char c : chars) {
                if (c == '{') {
                    ++brace;
                    isIncremented = true;
                } else if (c == '}') {
                    --brace;
                }
                sb.append(c);

                if (brace < 0) {
                    throw new IOException("Illegal JSON Content");
                }
                if (brace == 0) {
                    if (isIncremented) {
                        resultList.add(sb.toString());
                    }
                    sb.setLength(0);
                    isIncremented = false;
                }
            }

            if (brace != 0 || sb.length() > 0) {
                throw new IOException("Illegal JSON Content");
            }

            return resultList;
        }
    }

    public static List<String[]> loadSql(MySQLConnection mySQLConnection, String sql) throws ClassNotFoundException, SQLException {
        final String[] titles;
        final LinkedList<String[]> resultList = new LinkedList<>();

        Class.forName(mySQLConnection.getDriverName());
        try (Connection connection = DriverManager.getConnection(mySQLConnection.getJdbcUrl(), mySQLConnection.getUsername(), mySQLConnection.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            int columnCount = resultSet.getMetaData().getColumnCount();

            titles = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                titles[i - 1] = resultSet.getMetaData().getColumnLabel(i);
            }

            while (resultSet.next()) {
                String[] content = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    content[i - 1] = resultSet.getString(i);
                }
                resultList.add(content);
            }
        }

        resultList.addFirst(titles);

        return resultList;
    }
}
