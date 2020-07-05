/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.journal.journal.ws.rest.converter;

import com.journal.journal.bean.Issue;
import com.journal.journal.service.util.converter.IntConverter;
import com.journal.journal.ws.rest.vo.IssueVo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anoir
 */
public class IssueConverter implements AbstractConverter<Issue, IssueVo> {

    @Override
    public Issue toItem(IssueVo vo) {
        if (vo == null) {
            return null;
        } else {
            Issue issue = new Issue();
            try {
                issue.setId(vo.getId());
                issue.setIssn(vo.getIssn());
                issue.setNumber(IntConverter.stringToInt(vo.getNumber()));
                issue.setStartMonth(vo.getStartMonth());
                issue.setEndMonth(vo.getEndMonth());
                issue.setPublishDate(new SimpleDateFormat("yyyy-MM-dd").parse(vo.getPublishDate()));
                issue.setVolume(new VolumeConverter().toItem(vo.getVolumeVo()));
            } catch (ParseException ex) {
                Logger.getLogger(IssueConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return issue;
        }
    }

    @Override
    public IssueVo toVO(Issue t) {
        if (t == null) {
            return null;
        } else {
            IssueVo issueVo = new IssueVo();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            issueVo.setId(t.getId());
            issueVo.setIssn(t.getIssn());
            issueVo.setNumber(IntConverter.intTostring(t.getNumber()));
            issueVo.setStartMonth(t.getStartMonth());
            issueVo.setEndMonth(t.getEndMonth());
            issueVo.setPublishDate(dateFormat.format(issueVo.getPublishDate()));
            issueVo.setVolumeVo(new VolumeConverter().toVO(t.getVolume()));

            return issueVo;
        }
    }

}
