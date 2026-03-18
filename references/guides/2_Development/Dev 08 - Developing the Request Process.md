# Dev 08 - Developing the Request Process

## 1. Before You Start 
* After 'Asset Transfer Authorization', you must complete the process with either  'Report Transfer Result (TX Hash)' or 'Finish Transfer'.
* If the asset transfer is canceled, the status cannot be changed and you must restart from 'Asset Transfer Authorization'.
* Treat any response outside of the API specifications as an error and contact the CodeVASP team if it persists.
* Always save the generated 'transferId'.
* The receiving VASP may manage a list of VASPs it accepts assets from (especially Korean VASPs).
    * A VASP outside of the list will get a rejection or error at the 'Virtual Asset Address Search' or 'Asset Transfer Authorization' process.

![](Dev%2008%20-%20Developing%20the%20Request%20Process%20-%2001.png)![](Dev%2008%20-%20Developing%20the%20Request%20Process%20-%2002.png)