/*
 ***************************************************************
 *                                                             *
 *                           NOTICE                            *
 *                                                             *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             *
 *   CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   *
 *   OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  *
 *   WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       *
 *   ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  *
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            *
 *   ALL OTHER RIGHTS RESERVED.                                *
 *                                                             *
 *   (c) COPYRIGHT 2020 INFOR.  ALL RIGHTS RESERVED.           *
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            *
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          *
 *   AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        *
 *   RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         *
 *   THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  *
 *                                                             *
 ***************************************************************
 */

/*
 *Modification area - M3
 *Nbr               Date      User id     Description
 *ABF_R_200         20220405  RDRIESSEN   Mods BF0200- Write/Update EXTAPR records as a basis for PO authorization process
 *
 */

import groovy.lang.Closure
 
 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
 import groovy.json.JsonSlurper;
 import java.math.BigDecimal;
 import java.math.RoundingMode;
 import java.text.DecimalFormat;



public class List extends ExtendM3Transaction {
  private final MIAPI mi;
  private final DatabaseAPI database;
  private final MICallerAPI miCaller;
  private final LoggerAPI logger;
  private final ProgramAPI program;
  private final IonAPI ion;
  
  //Input fields
  private String cono;
  private String puno;
  private String appr;
  private String asts;
  private int XXCONO;
  
  private String puno1;
  private String appr1;
  private String asts1;
  
  private String YYCONO;
  
  
  public List(MIAPI mi, DatabaseAPI database, MICallerAPI miCaller, LoggerAPI logger, ProgramAPI program, IonAPI ion) {
    this.mi = mi;
    this.database = database;
  	this.miCaller = miCaller;
  	this.logger = logger;
  	this.program = program;
	  this.ion = ion;
  }
  
  public void main() {
    
      listEXTAPR(puno)
    
  }
  
   def listEXTAPR(String puno) {
    
    
    int currentCompany = (Integer)program.getLDAZD().CONO
    YYCONO = currentCompany.toString();
    
        DBAction query = database.table("EXTAPR").index("00").selection("EXCONO", "EXPUNO", "EXAPPR", "EXASTS").build()
        DBContainer container = query.getContainer()
        container.set("EXCONO", currentCompany)
        query.readAll(container, 1, releasedItemProcessor)
   
  }
  
  
  Closure<?> releasedItemProcessor = { DBContainer container ->
   puno1 = container.get("EXPUNO")
   appr1 = container.get("EXAPPR")
   asts1 = container.get("EXASTS")
 
   mi.outData.put("CONO" , YYCONO)
    mi.outData.put("PUNO" , puno1)
    mi.outData.put("APPR" , appr1)
    mi.outData.put("ASTS" , asts1)
    mi.write()
  
   
   
}
  
  
}