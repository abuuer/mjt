/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.vo;

import java.util.List;

/**
 *
 * @author anoir
 */
public class VolumeVo {
    private Long id;
    private String number;
    private String year;
    private List<IssueVo> issuesVo;
}
