/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainwindow;

/**
 *
 * @author CS
 */
interface Size {
    
    public enum AllSize {
        XS, S, M, L, XL;
    }
    
    public enum NumSize {
        A(0), B(2), C(4), D(6), E(8), F(10);
        
        public final int i;
        
        NumSize(int i) {
            this.i = i;
        }
        public int getNumSize() {
            return this.i;
        }
       
    }
    
    public enum WaistSize {
        A("30W"),
        B("32W"),
        C("34W"),
        D("36W"),
        E("38W");
        
        private String s;
        
        WaistSize(String s) {
            this.s = s;
        }
        String getWaistSize() {
            return s;
        }
    }
    
}
