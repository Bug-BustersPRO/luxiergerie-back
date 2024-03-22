package com.luxiergerie.Controllers;

import com.luxiergerie.Domain.Entity.Role;
import com.luxiergerie.Domain.Repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @InjectMocks
    RoleController roleController;

    @Mock
    private RoleRepository roleRepository;

    @Test
    public void shouldAddANewRole_whenIUseAddANewRoleFromRoleController() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Role expectedRole = new Role(UUID.randomUUID(), "ROLE_EMPLOYEE", new ArrayList<>());
        when(roleRepository.save(expectedRole)).thenReturn(expectedRole);

        // Act
        Role addedRole = roleController.addANewRole(expectedRole);

        // Assert
        assertAll(
                () -> assertThat(addedRole).isNotNull(),
                () -> assertThat(addedRole.getId()).isEqualTo(expectedRole.getId()),
                () -> assertThat(addedRole.getName()).isEqualTo(expectedRole.getName())
        );
    }

}