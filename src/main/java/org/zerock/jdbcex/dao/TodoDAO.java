package org.zerock.jdbcex.dao;

import lombok.Cleanup;
import org.zerock.jdbcex.domain.TodoVO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    public String getTime() {

        String now = null;

        //try-with-resource 문. try(resource) 식으로 선언.
        //autoClose가 구현되어 있는 객체면 자동으로 close해준다.
        try(Connection connection = ConnectionUtil.INSTANCE.getConnection();
            // preparedStatement: sql문을 보낸다. 보안을 위해 sql문을 먼저 보낸 후 데이터를 나중에 보낸다.
            PreparedStatement preparedStatement = connection.prepareStatement("select now()");
            //resultSet: 데이터베이스에서 반환하는 결과를 담는 인터페이스.
            //executeUpdate는 DML, executeQuery는 query(select)를 반환.
            ResultSet resultSet = preparedStatement.executeQuery();
            ) {
            resultSet.next();

            now = resultSet.getString(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return now;
    }

    public String getTime2() throws Exception {
        //@Cleanup이 try-with-resource를 대체
        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement("select now()");
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        String now = resultSet.getString(1);
        return now;

    }

    public  void insert(TodoVO vo) throws Exception {
        // ?로 나중에 전달할 데이터를 지정
        String sql = "insert into tbl_todo (title, dueDate, finished) values (?,?,?)";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        //? 개수만큼 set. 0이 아닌 1부터 시작한다는 것에 유의할 것.
        preparedStatement.setString(1, vo.getTitle());
        preparedStatement.setDate(2, Date.valueOf(vo.getDueDate()));
        preparedStatement.setBoolean(3, vo.isFinished());

        preparedStatement.executeUpdate();

    }

    public List<TodoVO> selectAll() throws Exception {

        String sql = "select * from tbl_todo";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);
        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        List<TodoVO> list = new ArrayList<>();

        while (resultSet.next()) {
            TodoVO vo = TodoVO.builder()
                    .tno(resultSet.getLong("tno"))
                    .title(resultSet.getString("title"))
                    .dueDate(resultSet.getDate("dueDate").toLocalDate())
                    .finished(resultSet.getBoolean("finished"))
                    .build();

            list.add(vo);
        }
        return list;
    }
    public TodoVO selectOne(Long tno) throws Exception {

        String sql = "select * from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next(); //데이터를 읽기 전에 커서를 한칸 이동해야함. 커서는 현재 결과집합 한칸 앞에 있음.
        TodoVO vo = TodoVO.builder()
                .tno(resultSet.getLong("tno"))
                .title(resultSet.getString("title"))
                .dueDate(resultSet.getDate("dueDate").toLocalDate())
                .finished(resultSet.getBoolean("finished"))
                .build();

        return vo;
    }

    public void deleteOne(Long tno) throws Exception {

        String sql = "delete from tbl_todo where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setLong(1, tno);

        preparedStatement.executeUpdate();
    }

    public void updateOne(TodoVO todoVO) throws Exception {

        String sql = "update tbl_todo set title = ?, dueDate = ?, finished = ? where tno = ?";

        @Cleanup Connection connection = ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, todoVO.getTitle());
        preparedStatement.setDate(2, Date.valueOf(todoVO.getDueDate()));
        preparedStatement.setBoolean(3, todoVO.isFinished());
        preparedStatement.setLong(4, todoVO.getTno());

        preparedStatement.executeUpdate();
    }
}
