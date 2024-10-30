package org.zerock.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.jdbcex.dao.TodoDAO;
import org.zerock.jdbcex.domain.TodoVO;

import java.time.LocalDate;
import java.util.List;

public class TodoDAOTests {

    private TodoDAO todoDAO;

    @BeforeEach
    public void ready() {
        todoDAO = new TodoDAO();
    }

    @Test
    public void testTime() throws Exception {
        System.out.println(todoDAO.getTime2());
    }
    @Test
    public void testInsert() throws Exception {
        TodoVO todoVO = TodoVO.builder()
                .title("Sample Title...")
                .dueDate(LocalDate.of(2024,12,30))
                .build();

        todoDAO.insert(todoVO);
    }
    @Test
    public void testList() throws Exception {
        List<TodoVO> list = todoDAO.selectAll();
        list.forEach(vo->System.out.println(vo));
    }
    @Test
    public void  testSelectOne() throws Exception {
        Long tno = 2L; //L은 long type을 의미
        TodoVO vo = todoDAO.selectOne(tno);

        System.out.println(vo);
    }
    @Test
    public void  testDeleteOne() throws Exception {
        Long tno = 3L; //L은 long type을 의미
        todoDAO.deleteOne(tno);
    }

    @Test
    public void testUpdateOne() throws Exception {
        TodoVO todoVO = TodoVO.builder()
                .tno(1L)
                .title("updated Title...")
                .dueDate(LocalDate.of(2024,12,30))
                .build();

        todoDAO.updateOne(todoVO);

    }

}
