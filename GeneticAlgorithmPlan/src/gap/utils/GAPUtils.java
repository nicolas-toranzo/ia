/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gap.utils;

import jenes.chromosome.BitwiseChromosome;

/**
 *
 */
public class GAPUtils {
    public static GAPChromosome parseFromBitWiseChromosome(BitwiseChromosome crom){
        GAPChromosome pcrom = new GAPChromosome();
        
        pcrom.setTipoOperacion(crom.getBitValueAt(0)*2 + crom.getBitValueAt(1));
        pcrom.setDependencia(crom.getBitValueAt(2)*4 + crom.getBitValueAt(3)*2 + crom.getBitValueAt(4));
        pcrom.setEficiencia(crom.getBitValueAt(5)*2 + crom.getBitValueAt(6));
        pcrom.setTiempoUso(crom.getBitValueAt(7)*4 + crom.getBitValueAt(8)*2 + crom.getBitValueAt(9));

        return pcrom;
    }
}
