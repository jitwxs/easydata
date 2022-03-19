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
     * @return file content with pair <title_line, data_lines>
     */
    public static List<String> loadJson(final String path, final Charset charset) throws IOException {
        final List<String> resultList = new ArrayList<>();

        try (final InputStream inputStream = CLAZZ.getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IOException("InputStream Not Exist");
            }

            final String string = IOUtils.toString(inputStream, charset);
            final char[] chars = string.toCharArray();

            int brackets = 0, startIndex = 0;

            for (int i = 0; i < chars.length; i++) {
                final char c = chars[i];
                if (c == '{') {
                    brackets++;
                } else if (c == '}') {
                    brackets--;
                }

                if (brackets < 0) {
                    // illegal data, reset
                    brackets = 0;
                    startIndex = i;
                } else if (brackets == 0) {
                    final String subJson = string.substring(startIndex, i + 1);
                    resultList.add(subJson);
                }
            }
        }

        return resultList;
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
