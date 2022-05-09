package com.consultoriomedico.repository;

import com.consultoriomedico.domain.*;


import java.text.DateFormat;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import lombok.Builder;
import org.apache.log4j.Logger;

@Builder
public class RepoUsuariosImpl implements RepoUsuarios {
    public static final String USUARIO_TXT = "usuario.txt";
    public static final Logger log = Logger.getLogger(RepoUsuariosImpl.class);
    private static final DateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    /*  REFACTOR APLICADO
    public void grabar(Object object) {
        log.info("[RepoUsuariosImpl][grabar] Inicio de llamada grabación usuario");
        Usuario usuario;
        AzureDB azureDB = new AzureDB();
        if (object instanceof Paciente paciente) {
            usuario = Paciente.builder().id(paciente.getId())
                    .creadoEn(paciente.getCreadoEn())
                    .flagDoctor(paciente.isFlagDoctor())
                    .nombre(paciente.getNombre())
                    .direccion(paciente.getDireccion())
                    .telefono(paciente.getTelefono())
                    .email(paciente.getEmail())
                    .build();

        } else if (object instanceof Doctor doctor) {
            usuario = Doctor.builder().id(doctor.getId())
                    .creadoEn(doctor.getCreadoEn())
                    .idEspecialidad(doctor.getIdEspecialidad())
                    .flagDoctor(doctor.isFlagDoctor())
                    .nombre(doctor.getNombre())
                    .direccion(doctor.getDireccion())
                    .telefono(doctor.getTelefono())
                    .email(doctor.getEmail()).build();
        } else {
            usuario = null;
        }

        if (usuario != null) {
            int flagDoctorNumber = usuario.isFlagDoctor() ? 1 : 0;
            try (BufferedWriter usuarioTxt = new BufferedWriter(new FileWriter(USUARIO_TXT, true))) {
                usuarioTxt.newLine();
                usuarioTxt.append(String.valueOf(usuario.getId()));
                usuarioTxt.append(";");
                usuarioTxt.append(dt1.format(usuario.getCreadoEn()));
                usuarioTxt.append(";");
                usuarioTxt.append(String.valueOf(flagDoctorNumber));
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getNombre());
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getDireccion());
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getTelefono());
                usuarioTxt.append(";");
                usuarioTxt.append(usuario.getEmail());
                if (usuario.isFlagDoctor()) {
                    usuarioTxt.append(";");
                    assert usuario instanceof Doctor;
                    usuarioTxt.append(((Doctor) usuario).getEspecialidad());
                }
                sendMailConfirmation(usuario);
                //SmsSender.builder().build().sendSms(usuario.getTelefono(), "Se creo el usuario con Exito, KodigoClinica");
            } catch (Exception e) {
                log.error(e);
                log.info("[RepoUsuariosImpl][grabar] Error en la grabación");
            } finally {
                log.info("[RepoUsuariosImpl][grabar] Fin de llamada grabación usuario");
            }
        }
    }*/

    public void grabarPaciente(Paciente paciente){
        log.info("[RepoUsuariosImpl][grabar] Inicio de llamada grabación usuario");
        try {
            IAzureDB azureDB = new AzureDB();
            if (azureDB.insertPacienteStatement(paciente)) {
                sendMailConfirmation(paciente);
                //SmsSender.builder().build().sendSms(paciente.getTelefono(), "Se creo el usuario con Exito, KodigoClinica");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoUsuariosImpl][grabar] Error en la grabación");
        } finally {
            log.info("[RepoUsuariosImpl][grabar] Fin de llamada grabación usuario");
        }
    }

    public void grabarDoctor(Doctor doctor){
        log.info("[RepoUsuariosImpl][grabar] Inicio de llamada grabación usuario");
        try {
            IAzureDB azureDB = new AzureDB();
            if (azureDB.insertDoctorStatement(doctor)) {
                sendMailConfirmation(doctor);
                //SmsSender.builder().build().sendSms(doctor.getTelefono(), "Se creo el usuario con Exito, KodigoClinica");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoUsuariosImpl][grabar] Error en la grabación");
        } finally {
            log.info("[RepoUsuariosImpl][grabar] Fin de llamada grabación usuario");
        }
    }

    public static void sendMailConfirmation(Usuario usuario) {
        try {
            log.info("[RepoUsuariosImpl][sendMail] Enviando correo de confirmación");
            EmailSender sender = EmailSender.builder().build();
            sender.enviarConfirmacionUsuario(usuario);
            log.info("[RepoUsuariosImpl][sendMail] Correo enviado exitosamente de confirmación");
        } catch (Exception e) {
            log.error(e);
            log.info("[RepoUsuariosImpl][sendMail] Error al enviar el correo de confirmación");
        }
    }

    /*public List<Doctor> listarDoctores() {
        ArrayList<Doctor> listaDoctores = new ArrayList<>();

        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String line;
            while ((line = usuariotTxt.readLine()) != null) {
                String[] partesDeUsuario = line.split(";");
                if (partesDeUsuario.length > 1) {
                    boolean flagDoctor = Integer.parseInt(partesDeUsuario[2]) == 1;
                    if (flagDoctor) {
                        Doctor doctor = Doctor.builder()
                                .id(Integer.parseInt(partesDeUsuario[0]))
                                .creadoEn(dt1.parse(partesDeUsuario[1]))
                                .flagDoctor(true)
                                .nombre(partesDeUsuario[3])
                                .direccion(partesDeUsuario[4])
                                .telefono(partesDeUsuario[5])
                                .email(partesDeUsuario[6])
                                .idEspecialidad(Integer.parseInt(partesDeUsuario[7]))
                                .build();

                        listaDoctores.add(doctor);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listaDoctores;
    }*/

    public List<Doctor> listarDoctores(){
        log.info("[RepoUsuariosImpl][listarDoctores] Inicio de llamada listar doctores");
        List<Doctor> listDoctor = null;
        try {
            IAzureDB azureDB = new AzureDB();
            listDoctor = azureDB.selectDoctor(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoUsuariosImpl][listarDoctores] Error listando");
        } finally {
            log.info("[RepoUsuariosImpl][listarDoctores] Fin de llamada listar doctores");
        }
        return listDoctor;
    }

    /*public List<Paciente> listarPacientes() {
        ArrayList<Paciente> listaPacientes = new ArrayList<>();

        try (BufferedReader usuariotTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
            String line;
            while ((line = usuariotTxt.readLine()) != null) {
                String[] partesDeUsuario = line.split(";");
                if (partesDeUsuario.length > 1) {
                    boolean flagDoctor = Integer.parseInt(partesDeUsuario[2]) == 1;
                    if (!flagDoctor) {
                        Paciente paciente = Paciente.builder()
                                .id(Integer.parseInt(partesDeUsuario[0]))
                                .creadoEn(dt1.parse(partesDeUsuario[1]))
                                .flagDoctor(false)
                                .nombre(partesDeUsuario[3])
                                .direccion(partesDeUsuario[4])
                                .telefono(partesDeUsuario[5])
                                .email(partesDeUsuario[6])
                                .build();
                        listaPacientes.add(paciente);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return listaPacientes;
    }*/

    public List<Paciente> listarPacientes(){
        log.info("[RepoUsuariosImpl][listarPacientes] Inicio de llamada listar pacientes");
        List<Paciente> listPaciente = null;
        try {
            IAzureDB azureDB = new AzureDB();
            listPaciente = azureDB.selectPaciente(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoUsuariosImpl][listarPacientes] Error listando");
        } finally {
            log.info("[RepoUsuariosImpl][listarPacientes] Fin de llamada listar pacientes");
        }
        return listPaciente;
    }

    public List<Doctor> listarDoctoresPorEspecialidad(int especialidad) {
        ArrayList<Doctor> listDoctores = (ArrayList<Doctor>) listarDoctores();
        ArrayList<Doctor> listDoctoresEspecialidad = new ArrayList<>();
        for (Doctor doctor : listDoctores) {
            if (doctor.getIdEspecialidad() == especialidad)
                listDoctoresEspecialidad.add(doctor);
        }
        return listDoctoresEspecialidad;
    }

    /*public Usuario buscarPorId(int id) {
        Usuario usuario = null;
        if (new File(USUARIO_TXT).exists()) {
            try (BufferedReader usuarioTxt = new BufferedReader(new FileReader((USUARIO_TXT)))) {
                String line;
                while ((line = usuarioTxt.readLine()) != null) {
                    String[] partesDeUsuario = line.split(";");
                    if (partesDeUsuario.length > 1 && id == Integer.parseInt(partesDeUsuario[0])) {
                        usuario = Usuario.builder()
                                .id(Integer.parseInt(partesDeUsuario[0]))
                                .creadoEn(dt1.parse(partesDeUsuario[1]))
                                .flagDoctor(Integer.parseInt(partesDeUsuario[2]) == 1)
                                .nombre(partesDeUsuario[3])
                                .direccion(partesDeUsuario[4])
                                .telefono(partesDeUsuario[5])
                                .email(partesDeUsuario[6])
                                .build();
                        break;
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return usuario;
    }*/

    public Doctor buscarDoctorPorId(int id) {
        log.info("[RepoUsuariosImpl][buscarDoctorPorId] Inicio de llamada buscar paciente por ID:");
        List<Doctor> listDoctor = null;
        try {
            IAzureDB azureDB = new AzureDB();
            listDoctor = azureDB.selectDoctor(true, Integer.toString(id));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoUsuariosImpl][buscarDoctorPorId] Error listando");
        } finally {
            log.info("[RepoUsuariosImpl][buscarDoctorPorId] Fin de llamada buscar doctor por Id");
        }
        return (listDoctor != null) ? listDoctor.get(0) : null;
    }

    public Paciente buscarPacientePorId(int id) {
        log.info("[RepoUsuariosImpl][buscarPacientePorId] Inicio de llamada buscar paciente por ID:");
        List<Paciente> listPaciente = null;
        try {
            IAzureDB azureDB = new AzureDB();
            listPaciente = azureDB.selectPaciente(true, Integer.toString(id));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoUsuariosImpl][buscarPacientePorId] Error listando");
        } finally {
            log.info("[RepoUsuariosImpl][buscarPacientePorId] Fin de llamada buscar doctor por Id");
        }
        return (listPaciente != null) ? listPaciente.get(0) : null;
    }

    public List<Especialidad> listarEspecialidades(){
        List<Especialidad> listEspecialidad = null;
        try {
            IAzureDB azureDB = new AzureDB();
            listEspecialidad = azureDB.listEspecialidades();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            log.info("[RepoUsuariosImpl][buscarPacientePorId] Error listando");
        } finally {
            log.info("[RepoUsuariosImpl][buscarPacientePorId] Fin de llamada buscar doctor por Id");
        }
        return listEspecialidad;
    }
}