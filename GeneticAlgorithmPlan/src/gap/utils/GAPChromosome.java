/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gap.utils;

/**
 *
 */
public class GAPChromosome {
    
    private Integer tipoOperacion;
    private Integer dependencia;
    private Integer eficiencia;
    private Integer tiempoUso;

    public Integer getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(Integer tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Integer getDependencia() {
        return dependencia;
    }

    public void setDependencia(Integer dependencia) {
        this.dependencia = dependencia;
    }

    public Integer getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(Integer eficiencia) {
        this.eficiencia = eficiencia;
    }

    public Integer getTiempoUso() {
        return tiempoUso;
    }

    public void setTiempoUso(Integer tiempoUso) {
        this.tiempoUso = tiempoUso;
    }

    @Override
    public String toString() {
        return "Tipo Operación: " + String.valueOf(this.tipoOperacion)
                + "\r\n" + 
                "Dependencia: " + String.valueOf(this.dependencia)
                + "\r\n" + 
                "Eficiencia: " + String.valueOf(this.eficiencia)
                + "\r\n" + 
                "Tiempo de uso: " + String.valueOf(this.tiempoUso);
    }
}
