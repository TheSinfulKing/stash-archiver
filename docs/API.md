# Backup Config
A Backup Config is an array of Backup objects. The Backup objects will be read in order. Each will be evaluated to identify matching Scenes or Images. Those Scenes or Images will then be checked amongst all backup paths specified in the Program Config. If they exist, they will be skipped. If they do not, they will be added to a Copy job to copy. Once all Backup Configs have been processed, the Copy job will execute.  

## **Backup Object** 
### **_Fields_**
| Member | Required | Definition | Notes | Type | Default
| ------ | --- | ---- | --- | --- | --- |
| `type` | No | Backup Type | Type of content to backup | **'scene'**, **'image'**, or **'both'** | both
| `performers` | No | Performer Names/Ids | Numeric entries will assume Performer StashId. If any non-numeric character is included, Performer Name will be assumed. (must match exactly, case insensitive) It is recommended to use Performer StashIds in case of future Performer Name changes. | String[] | -
