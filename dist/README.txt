
BLT, a graphic tool that use BGP Linkstate
 to grab and show your IGP database, 
 provided free of any charge by NetFishers.

For more information, visit http://www.netfishers.onl/blt
or contact us at contact@netfishers.onl.


RELEASE HISTORY:

0.3.2 - 2015-04-16 - The initial public release.
0.3.3 - 2015-04-19 - Add prefix add/withdraw monitoring capability
0.3.4 - 2015-07-20 - Add graphic monitoring capability:
                     - routers and prefixes turn green/orange/red upon announcement and withdrawal
                     - customizable timers in "blt.conf" blt.bgp.deltaNewIgpRoute and blt.bgp.deltaLostIgpRoute
                     Add age of routes in "prefix" tab
                     Elliptic layout of routers at launch
0.3.5 - 2015-11-21 - Add an auto refresh feature for real time RIB monitoring.
                     This can be toggle on/off from the main window toolbar. 
                   - Fix some bugs:
                     - now routers positions are saved in XML dumpfile as soon as the layout is updated
                     - SnmpPollingTask : we need to retrieve SNMP router hostname at least once when IGP is OSPF
                     
