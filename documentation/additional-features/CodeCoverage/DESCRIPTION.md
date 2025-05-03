### 🎯 CC-1: Implement Code Coverage with JaCoCo and GitHub Actions

**Objective**  
Integrate code coverage analysis into our Spring Boot project using [JaCoCo](https://www.jacoco.org/jacoco/) and enforce a minimum of 80% instruction coverage. The goal is to ensure that all new code meets quality standards and maintains adequate test coverage.

---

### ✅ Requirements

- Integrate `jacoco-maven-plugin` into the `pom.xml`.
- Configure JaCoCo to:
    - Attach to the Maven test lifecycle.
    - Generate a coverage report in HTML format.
    - Enforce a minimum threshold of **80% instruction coverage**.
- Create a GitHub Actions workflow to:
    - Build the project with Maven.
    - Run tests and generate the coverage report.
    - Fail the build if coverage is below 80%.

---

### 📁 Output

- Coverage report will be generated at: `target/site/jacoco/index.html`
- GitHub Action will fail if the minimum coverage requirement is not met.

---

### 🧪 Acceptance Criteria

- ✅ Project builds successfully with JaCoCo integrated.
- ✅ Coverage report is generated and accessible.
- ✅ GitHub Action fails if instruction coverage is < 80%.
- ✅ Code reviewers can rely on coverage status to enforce test quality.
