package com.devlhse.minhasfinancas.api.resource;

import com.devlhse.minhasfinancas.api.dto.UsuarioDTO;
import com.devlhse.minhasfinancas.config.JwtConfig;
import com.devlhse.minhasfinancas.exception.RegraNegocioException;
import com.devlhse.minhasfinancas.model.entity.Usuario;
import com.devlhse.minhasfinancas.service.LancamentoService;
import com.devlhse.minhasfinancas.service.UsuarioService;
import com.devlhse.minhasfinancas.service.impl.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest( controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

    private static final String URL = "/api/usuarios";
    private static final String EMAIL_VALIDO = "usuario@email.com";
    private static final String SENHA_VALIDA = "123";
    private static final String NOME_VALIDO = "usuario";

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    LancamentoService lancamentoService;

    @MockBean
    JwtConfig jwtConfig;


    @Test
    public void deveCriarUmUsuario() throws Exception {
        UsuarioDTO dto = UsuarioDTO.builder().nome(NOME_VALIDO).email(EMAIL_VALIDO).senha(SENHA_VALIDA).build();
        Usuario usuario = Usuario.builder().id(1l).nome(NOME_VALIDO).email(EMAIL_VALIDO).senha(SENHA_VALIDA).build();

        when(service.salvar(any(Usuario.class))).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }

    @Test
    public void deveRetornarBadRequestAoTentarCriarUmUsuario() throws Exception {
        UsuarioDTO dto = UsuarioDTO.builder().nome(NOME_VALIDO).email(EMAIL_VALIDO).senha(SENHA_VALIDA).build();

        when(service.salvar(any(Usuario.class))).thenThrow(RegraNegocioException.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
