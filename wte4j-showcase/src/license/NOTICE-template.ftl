<#--

    Copyright (C) 2015 Born Informatik AG (www.born.ch)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

            http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<#-- To render the third-party file.

Available context : see

 

- dependencyMap a collection of Map.Entry with

  key are dependencies (as a MavenProject) (from the maven project)

  values are licenses of each dependency (array of string)

 

- licenseMap a collection of Map.Entry with

  key are licenses of each dependency (array of string)

  values are all dependencies using this license

-->
<#function licenseFormat licenses>

   <#assign result = ""/>

   <#list licenses as license>

       <#assign result = result + " " +license + ""/>

   </#list>

   <#return result>

</#function>
<#function artifactFormat p>

   <#if p.name?index_of('Unnamed') &gt; -1>

       <#return p.artifactId + " (" + p.groupId + ":" + p.artifactId + ":" + p.version + " - " + (p.url!"no url defined") + ")">

   <#else>

       <#return p.name + " (" + p.groupId + ":" + p.artifactId + ":" + p.version + " - " + (p.url!"no url defined") + ")">

   </#if>

</#function>
<#--

-->
Copyright 2015, Born Informatik AG.

This project includes software developed by Born Informatik AG.
http://www.born.ch/

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This software includes unchanged copies of the following libraries:
   Apache XBean :: ASM 4 shaded (xbean:org.apache.xbean:) under the The Apache Software License, Version 2.0
   Streaming API for XML (javax.xml.stream:stax-api:jar:1.0-2) under COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0
   <#list dependencyMap as e>
       <#assign project = e.getKey()/>
       <#assign licenses = e.getValue()/>
   ${artifactFormat(project)} under ${licenseFormat(licenses)} 
   </#list>


