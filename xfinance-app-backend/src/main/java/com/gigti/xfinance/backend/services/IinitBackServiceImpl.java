package com.gigti.xfinance.backend.services;

import com.gigti.xfinance.backend.data.*;
import com.gigti.xfinance.backend.others.Constantes;
import com.gigti.xfinance.backend.others.HasLogger;
import com.gigti.xfinance.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class IinitBackServiceImpl implements IinitBackService, HasLogger {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TipoIdeRepository tipoIdeRepository;

    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ParcheRepository parcheRepository;
    @Autowired
    private TipoEmpresaRepository tipoEmpresaRepository;
    //private PasswordEncoder passwordEncoder;


    // Metodo para crear Init del Backend
    // Tipos de Datos
    @Transactional
    @Override
    public void initBackTipos() {
        try {
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT1, null);
            if (parche == null) {
                //Tipo Ide
                tipoIdeRepository.save(TipoIde.CEDULA);
                tipoIdeRepository.save(TipoIde.EXTRANJERIA);
                tipoIdeRepository.save(TipoIde.NIT);
                tipoIdeRepository.save(TipoIde.TIDENTIDAD);

                //Tipo Usuarios
                tipoUsuarioRepository.save(TipoUsuario.ADMIN);
                tipoUsuarioRepository.save(TipoUsuario.NORMAL);
                tipoUsuarioRepository.save(TipoUsuario.ROOT);
                tipoUsuarioRepository.save(TipoUsuario.SELLER);

                parche = new Parche(Constantes.INIT1,java.sql.Date.valueOf(LocalDate.now()),true, null);
                parcheRepository.save(parche);
            }
        } catch (Exception e) {
            getLogger().error("Error al Crear InitBackend - Tipos: " + e.getMessage(), e);
        }
    }
    //Usuarios Root y Admin
    // Dummys de Pruebas ETC.
    @Transactional
    @Override
    public void initBackObjetos() {
        try{
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT2, null);
            if (parche == null) {
                //Roles
                Rol rolDefault = rolRepository.save(Rol.ROOT);

                //Compañia GigTi
                Empresa emp = new Empresa(
                        TipoIde.NIT,
                        "900800",
                        "Gig Ti S.A.S",
                        "Calle 191A # 11A-92",
                        true,
                        java.sql.Date.valueOf(LocalDate.now()),
                        null,
                        TipoEmpresa.ROOT);

                emp = empresaRepository.save(emp);

                Persona persona = new Persona(
                        TipoIde.CEDULA,
                        "73008913",
                        "Luis",
                        "José",
                        "Villarreal",
                        "Rincón",
                        java.sql.Date.valueOf(LocalDate.of(1985, 6, 11)),
                        "ljosevr@gmail.com",
                        "3006520012",
                        "calle 191A#11A-92"
                );

                persona = personaRepository.save(persona);

                //Usuario Root
                Usuario userRoot = new Usuario(
                        "Root",
                        //passwordEncoder.encode("1234"),
                        "1234",
                        true,
                        persona,
                        emp,
                        rolDefault,//rolDefault,
                        TipoUsuario.ROOT
                );

                usuarioRepository.save(userRoot);

                parche = new Parche(Constantes.INIT2,java.sql.Date.valueOf(LocalDate.now()),true, null);
                parcheRepository.save(parche);
            }
        }catch(Exception e){
            getLogger().error("Error al Crear InitBackend - Objetos: "+e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public void initBackTipoEmpresa() {
        try {
            Parche parche = parcheRepository.findByNombreAndEmpresa(Constantes.INIT3, null);
            if (parche == null) {
                //Tipo Ide
                tipoEmpresaRepository.save(TipoEmpresa.ROOT);
                tipoEmpresaRepository.save(TipoEmpresa.NORMAL);

                parche = new Parche(Constantes.INIT3,java.sql.Date.valueOf(LocalDate.now()),true, null);
                parcheRepository.save(parche);
            }
        } catch (Exception e) {
            getLogger().error("Error al Crear InitBackend - Tipo Empresa: " + e.getMessage(), e);
        }
    }
}
