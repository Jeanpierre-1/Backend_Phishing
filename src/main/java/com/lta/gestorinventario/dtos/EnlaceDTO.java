package com.lta.gestorinventario.dtos;

public class EnlaceDTO {
    private Long id;
    private String url;
    private String aplicacion;
    private String mensaje;
    private Long usuarioId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    public EnlaceDTO() {
    }

    public EnlaceDTO(Long id, String url, String aplicacion, String mensaje, Long usuarioId) {
        this.id = id;
        this.url = url;
        this.aplicacion = aplicacion;
        this.mensaje = mensaje;
        this.usuarioId = usuarioId;
    }


}
