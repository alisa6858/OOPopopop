package ru.lab5.manual.dto;

import org.junit.jupiter.api.Test;
import ru.lab5.manual.dto.mapper.RecordToDtoMapper;
import ru.lab5.manual.model.FunctionRecord;
import ru.lab5.manual.model.PermissionRecord;
import ru.lab5.manual.model.PointRecord;
import ru.lab5.manual.model.UserRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты маппера Record → DTO.
 */
class RecordToDtoMapperTest {

    private final RecordToDtoMapper mapper = new RecordToDtoMapper();

    @Test
    void testUserMapping() {
        UserRecord record = new UserRecord(1L, "test_user", "secret", 1);
        UserDto dto = mapper.toUserDto(record);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("test_user", dto.getUsername());
        assertEquals(1, dto.getRole());
    }

    @Test
    void testFunctionMapping() {
        FunctionRecord record = new FunctionRecord(10L, 1L, "f(x)", "описание");
        FunctionDto dto = mapper.toFunctionDto(record);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getOwnerId());
        assertEquals("f(x)", dto.getName());
    }

    @Test
    void testPointMapping() {
        PointRecord record = new PointRecord(5L, 10L, 3, 1.5, 2.5);
        PointDto dto = mapper.toPointDto(record);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals(10L, dto.getFunctionId());
        assertEquals(3, dto.getIndex());
        assertEquals(1.5, dto.getX(), 1e-9);
        assertEquals(2.5, dto.getY(), 1e-9);
    }

    @Test
    void testPermissionMapping() {
        PermissionRecord record = new PermissionRecord(2L, 10L, 20L, 1);
        PermissionDto dto = mapper.toPermissionDto(record);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(20L, dto.getFunctionId());
        assertEquals(1, dto.getAccess());
    }

    @Test
    void testListMapping() {
        List<UserRecord> records = List.of(
                new UserRecord(1L, "u1", "p1", 0),
                new UserRecord(2L, "u2", "p2", 1)
        );
        List<UserDto> dtos = mapper.toUserDtoList(records);

        assertEquals(2, dtos.size());
        assertEquals("u1", dtos.get(0).getUsername());
        assertEquals("u2", dtos.get(1).getUsername());
    }
}
