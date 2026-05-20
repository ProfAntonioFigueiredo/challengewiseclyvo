================================================================================
Readme - AWR (Automatic Workload Repository) Miner Extract    Updated 10/30/2025
================================================================================

This is the brief guide to using this tool to collect data for input into the 
internal Oracle Cloud Capacity Analytics (OCCA) sizing tool.  For a deeper look 
at all the steps please go to the OCCA Extracts User's Guide PDF that should be 
included in the folder with this file.

Step 1: Unzip the file, provided by your Oracle contact
--------------------------------------------------------------------------------

- Since you are here, you probably completed this step.
- These files need to be on a host/desktop with access to the target database.
- In this case you will need to connect to one or more target databases so, you 
  can either run from each host where database is or any other host with the 
  SQL*Plus and Oracle Net access to each target database.

Step 2: Identify and Verify Prerequisites
--------------------------------------------------------------------------------

- Oracle Database versions 11.2 and higher.
- Connect Strings to each target database (if CDB you need to connect to the 
  CDB root container), or if running on the DB Server host a direct connection 
  (e.g., / as sysdba) can be used.
- Database Account username and password, if not using OS authentication, for 
  each target database with sufficient access.  Accounts with DBA or SYSDBA 
  privileges can be used, at a minimum the following privileges are needed: 
  create session and select_catalog_role
- If planning on using either the Source Side Obfuscation feature you will 
  need Python 3.6 or higher

Step 3: Run Collection Script
--------------------------------------------------------------------------------

- Set Oracle Environment Variables
- Run the SQL*Plus like any other.
    sqlplus c##readuser/XXXX@hostname.acme.com/oradb.acme.com @awr_miner.sql
    sqlplus / as sysdba @awr_miner.sql
- Example Execution shown below.
- Output goes file, name format: awr-hist-<DBID>-<DBNAME>-<BEGIN>-<END>.out
- Please do not rename files, as it will break the Oracle processing.

Step 3a: Optional - Run Source Side Obfuscation
--------------------------------------------------------------------------------

- Not recommended unless required by your business.
- See Appendix in the OCCA Extracts User's Guide

Step 4: Zip output files and upload to Oracle
--------------------------------------------------------------------------------

- Collect all the output files and zip them up.
- Oracle uses SharePoint to share files back and forth with customers, your 
  Oracle contact should have provided you a link to upload your files, please 
  let them know when the files are uploaded.

================================================================================
End of Readme
================================================================================

Example AWR Miner Execution
--------------------------------------------------------------------------------

$ sqlplus c##readuser/XXXX@hostname.acme.com/oradb.acme.com @awr_miner.sql
SQL*Plus: Release 19.0.0.0.0 - Production on Thu Oct 30 15:32:00 2025
Version 19.25.0.0.0
Copyright (c) 1982, 2024, Oracle.  All rights reserved.
Last Successful login time: Tue Oct 28 2025 11:20:54 -05:00
Connected to:
Oracle Database 19c Enterprise Edition Release 19.0.0.0.0 - Production
Version 19.25.0.0.0
+-------------------------------------------------------------------+
|  ###   ###  ###   ####                                            |
| #   # #    #     #   #                 _                          |
| #   # #    #    ######     /\   |  |  |_)   |\/|  o   _    _   ,_ |
|  ###  ###  ### ##   ##    /--\  |/\|  | \   |  |  |  | |  (/_  |  |
+-------------------------------------------------------------------+
AWR-Miner Version 25.2.0
Collects metrics and statistics from AWR History of individual Oracle Databases.
Database Ids in this AWR Repository:
-------------------------------------------------------------------------------
Database ID         Database Name       Database Unique Name     Database Role
-----------         -------------       --------------------     -------------
1532163675          ACME                ACME                     PRIMARY
Execution Environment:
-------------------------------------------------------------------------------
Database ID         : 1532163575
Database Name       : ACME
Database Version    : 19
Database Block Size : 8192
Min Snapshot ID     : 6708
Max Snapshot ID     : 6919
Collect last n days : 30
Time Waited Column  : TIME_WAITED_MICRO_FG
Paralellisim Degree : 8
-------------------------------------------------------------------------------
Starting Extract Script!
Extracting OS information...
Extracting DB patch History...
Extracting Module...
Extracting DB AWR snapshots...
Extracting DB SGA and PGA settings...
Extracting DB SGA Advice...
Extracting DB PGA Advice...
Extracting DB size...
Extracting OS statistics...
Extracting Host metrics...
Extracting SqlNet metrics...
Extracting DB parameters...
Extracting DB AWR snapshots timezone information...
Extracting DB sessions..
Extracting Histogram by Wait Event...
Extracting IO segment statistics by object type...
Extracting IO statistics by function...
Extracting Waits by event...
Extracting Host IO statistics...
Extracting Segment IO historical statistics...
Extracting SQL statements...
Extracting SQL statements by snapshot...
Extracting Row counts...
Completed Extract Script!
Collection File : awr-hist-1532163675-ACME-6708-6919.out
-------------------------------------------------------------------------------
Disconnected from Oracle Database 19c Enterprise Edition Release 19.0.0.0.0 - Production
Version 19.25.0.0.0






