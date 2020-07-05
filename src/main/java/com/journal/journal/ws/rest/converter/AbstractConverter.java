/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.converter;

/**
 *
 * @author anoir
 */
public interface AbstractConverter<T, VO> {
    
    public T toItem(VO vo);
    
    public VO toVO(T t);
    
    //public List<T> toItems(VO vos);
    
   // public VO toVO(T t);
}
