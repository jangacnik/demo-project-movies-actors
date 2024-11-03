<h2>Demo Movie / Actor Service Project</h3>

Demo project in Java 21 and Spring Boot 3
<hr>
<h4>How to run each project</h4>
Use the given run configuration for each service:<br>

[Movie Service Local Run Configuration](./.run/MovieApplication.run.xml)
<br>

[Actor Service Local Run Configuration](./.run/ActorApplication.run.xml)
<hr>

<h4>How to package each project</h4>
Use the given run configuration for each service:<br>
[Movie Service Package Configuration](./.run/movie%20%5Bpackage%5D.run.xml)
<br>

[Actor Service Package Configuration](./.run/actor%20%5Bpackage%5D.run.xml)
<hr>
<h4>Docker deployment</h4>
For deployment of both services with included MongoDB databases use:<br>

[Docker Compose file](./compose.yaml)
<br> with the following command (compose + build):
```sh
docker-compose up --build
```

Note:
<ul>
<li>Both services need to be packaged before using compose.yaml</li>
<li>For ease of demonstration dockerfiles include jasypt secrets for docker profile property files (usually should not be added to repo)
</li>
</ul>
<hr>
<h4>To-Do / Possible improvements </h4>
<ul>
<li>Use parent pom for dependency management (services use same dependencies)
</li>
<li>Create shared library for sharing common methods and models/dto
</li>
<li>Configure test DB for better test coverage
</li>
<li>Add filter function for filtering movies/actors
</li>
</ul>
<hr>
<h4>Testing of endpoints with Postman</h4>

[Postman collection](./Demo.postman_collection.json)
includes http request for testing both services for develop and docker profile

