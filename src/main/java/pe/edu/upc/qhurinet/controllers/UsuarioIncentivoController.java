package pe.edu.upc.qhurinet.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.qhurinet.dtos.UsuarioIncentivoDTO;
import pe.edu.upc.qhurinet.dtos.UsuarioIncentivoProgresoDTO;
import pe.edu.upc.qhurinet.dtos.UsuarioIncentivoRecordatorioDTO;
import pe.edu.upc.qhurinet.entities.Incentivo;
import pe.edu.upc.qhurinet.entities.Notificacion;
import pe.edu.upc.qhurinet.entities.TransaccionPuntos;
import pe.edu.upc.qhurinet.entities.Usuario;
import pe.edu.upc.qhurinet.entities.UsuarioIncentivo;
import pe.edu.upc.qhurinet.securities.SecurityUserResolver;
import pe.edu.upc.qhurinet.servicesinterfaces.IIncentivoService;
import pe.edu.upc.qhurinet.servicesinterfaces.INotificacionService;
import pe.edu.upc.qhurinet.servicesinterfaces.ITransaccionPuntosService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioIncentivoService;
import pe.edu.upc.qhurinet.servicesinterfaces.IUsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-incentivos")
@PreAuthorize("hasAnyRole('ADMIN','USER','GENERADOR','RECOLECTOR','BODEGA')")
public class UsuarioIncentivoController {
    @Autowired
    private IUsuarioIncentivoService uIS;

    @Autowired
    private IUsuarioService uS;

    @Autowired
    private IIncentivoService iS;

    @Autowired
    private ITransaccionPuntosService tS;

    @Autowired
    private INotificacionService nS;

    @Autowired
    private SecurityUserResolver securityUserResolver;

    @GetMapping({"", "/lista"})
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioIncentivoDTO> list() {
        return uIS.list().stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public UsuarioIncentivoDTO listId(@PathVariable Long id) {
        UsuarioIncentivo usuarioIncentivo = uIS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario incentivo no encontrado"));
        securityUserResolver.requireAdminOrUser(usuarioIncentivo.getUsuario().getId());
        return toDTO(usuarioIncentivo);
    }

    @GetMapping("/progreso/{idUsuario}")
    public List<UsuarioIncentivoProgresoDTO> progresoIncentivosUsuario(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        return uIS.progresoIncentivosUsuario(idUsuario).stream().map(this::toProgresoDTO).toList();
    }

    @GetMapping("/recordatorios/{idUsuario}")
    public List<UsuarioIncentivoRecordatorioDTO> recordatoriosIncentivosUsuario(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        return uIS.recordatoriosIncentivosUsuario(idUsuario).stream().map(this::toRecordatorioDTO).toList();
    }

    @PostMapping("/recompensa-diaria/{idUsuario}")
    public java.util.Map<String, Object> reclamarRecompensaDiaria(@PathVariable Long idUsuario) {
        securityUserResolver.requireAdminOrUser(idUsuario);
        if (tS.recibioRecompensaDiariaHoy(idUsuario)) {
            throw new IllegalArgumentException("La recompensa diaria ya fue reclamada hoy");
        }
        Usuario usuario = uS.listId(idUsuario).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        int puntos = 5;
        usuario.setPuntosTotales((usuario.getPuntosTotales() == null ? 0 : usuario.getPuntosTotales()) + puntos);
        usuario.setNivelParticipacion(calcularNivel(usuario.getPuntosTotales()));
        uS.update(usuario);

        TransaccionPuntos tx = new TransaccionPuntos();
        tx.setUsuario(usuario);
        tx.setTipo("ganado");
        tx.setPuntos(puntos);
        tx.setMotivo("Recompensa diaria");
        tx.setReferenciaTipo("recompensa_diaria");
        tx.setReferenciaId(usuario.getId());
        tS.insert(tx);

        crearNotificacion(usuario, "recompensa", "Recompensa diaria reclamada", "Ganaste " + puntos + " puntos por tu recompensa diaria");
        return java.util.Map.of(
                "idUsuario", idUsuario,
                "puntosGanados", puntos,
                "puntosTotales", usuario.getPuntosTotales(),
                "nivelParticipacion", usuario.getNivelParticipacion()
        );
    }

    @PostMapping({"", "/nuevo"})
    public UsuarioIncentivoDTO insert(@RequestBody UsuarioIncentivoDTO dto) {
        securityUserResolver.requireAdminOrUser(dto.getIdUsuario());
        return toDTO(uIS.insert(toEntity(new UsuarioIncentivo(), dto)));
    }

    @PutMapping("/{id}")
    public UsuarioIncentivoDTO update(@PathVariable Long id, @RequestBody UsuarioIncentivoDTO dto) {
        UsuarioIncentivo usuarioIncentivo = uIS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario incentivo no encontrado"));
        securityUserResolver.requireAdminOrUser(usuarioIncentivo.getUsuario().getId());
        uIS.update(toEntity(usuarioIncentivo, dto));
        return toDTO(usuarioIncentivo);
    }

    @PutMapping("/actualiza")
    public UsuarioIncentivoDTO update(@RequestBody UsuarioIncentivoDTO dto) {
        return update(dto.getId(), dto);
    }

    @PutMapping("/{id}/progreso/{cantidadActual}")
    public UsuarioIncentivoDTO actualizarProgreso(@PathVariable Long id, @PathVariable Integer cantidadActual) {
        UsuarioIncentivo usuarioIncentivo = uIS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario incentivo no encontrado"));
        securityUserResolver.requireAdminOrUser(usuarioIncentivo.getUsuario().getId());
        return toDTO(uIS.actualizarProgreso(id, cantidadActual));
    }

    @PutMapping("/{id}/reclamar")
    public UsuarioIncentivoDTO reclamar(@PathVariable Long id) {
        UsuarioIncentivo usuarioIncentivo = uIS.listId(id).orElseThrow(() -> new IllegalArgumentException("Usuario incentivo no encontrado"));
        securityUserResolver.requireAdminOrUser(usuarioIncentivo.getUsuario().getId());
        UsuarioIncentivo reclamado = uIS.reclamar(id);
        crearNotificacion(reclamado.getUsuario(), "logro", "Incentivo reclamado", "Reclamaste el incentivo " + reclamado.getIncentivo().getNombre());
        return toDTO(reclamado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        uIS.delete(id);
    }

    private UsuarioIncentivo toEntity(UsuarioIncentivo usuarioIncentivo, UsuarioIncentivoDTO dto) {
        Usuario usuario = uS.listId(dto.getIdUsuario()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Incentivo incentivo = iS.listId(dto.getIdIncentivo()).orElseThrow(() -> new IllegalArgumentException("Incentivo no encontrado"));
        usuarioIncentivo.setUsuario(usuario);
        usuarioIncentivo.setIncentivo(incentivo);
        usuarioIncentivo.setCantidadActual(dto.getCantidadActual());
        usuarioIncentivo.setEstado(dto.getEstado());
        usuarioIncentivo.setCompletadoEn(dto.getCompletadoEn());
        return usuarioIncentivo;
    }

    private UsuarioIncentivoDTO toDTO(UsuarioIncentivo usuarioIncentivo) {
        ModelMapper m = new ModelMapper();
        UsuarioIncentivoDTO dto = m.map(usuarioIncentivo, UsuarioIncentivoDTO.class);
        dto.setIdUsuario(usuarioIncentivo.getUsuario().getId());
        dto.setUsuarioNombre(usuarioIncentivo.getUsuario().getNombre());
        dto.setIdIncentivo(usuarioIncentivo.getIncentivo().getId());
        dto.setIncentivoNombre(usuarioIncentivo.getIncentivo().getNombre());
        return dto;
    }

    private UsuarioIncentivoProgresoDTO toProgresoDTO(Object[] row) {
        return new UsuarioIncentivoProgresoDTO(
                ValueReader.asLong(row[0]),
                ValueReader.asLong(row[1]),
                ValueReader.asString(row[2]),
                ValueReader.asString(row[3]),
                ValueReader.asInteger(row[4]),
                ValueReader.asString(row[5]),
                ValueReader.asInteger(row[6]),
                ValueReader.asString(row[7]),
                ValueReader.asLocalDateTime(row[8]),
                ValueReader.asBoolean(row[9])
        );
    }

    private UsuarioIncentivoRecordatorioDTO toRecordatorioDTO(Object[] row) {
        return new UsuarioIncentivoRecordatorioDTO(
                ValueReader.asLong(row[0]),
                ValueReader.asLong(row[1]),
                ValueReader.asString(row[2]),
                ValueReader.asString(row[3]),
                ValueReader.asInteger(row[4]),
                ValueReader.asInteger(row[5]),
                ValueReader.asString(row[6]),
                ValueReader.asLocalDate(row[7]),
                ValueReader.asLocalDateTime(row[8]),
                ValueReader.asBoolean(row[9]),
                ValueReader.asBoolean(row[10])
        );
    }

    private void crearNotificacion(Usuario usuario, String tipo, String titulo, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuario);
        notificacion.setTipo(tipo);
        notificacion.setTitulo(titulo);
        notificacion.setMensaje(mensaje);
        notificacion.setEstado("pendiente");
        nS.insert(notificacion);
    }

    private String calcularNivel(Integer puntosTotales) {
        int puntos = puntosTotales == null ? 0 : puntosTotales;
        if (puntos >= 1000) {
            return "PLATINO";
        }
        if (puntos >= 500) {
            return "ORO";
        }
        if (puntos >= 100) {
            return "PLATA";
        }
        return "BRONCE";
    }
}
