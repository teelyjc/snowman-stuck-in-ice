plugins {
  application
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

dependencies {
  // Use JUnit test framework.
  testImplementation(libs.junit)

  // This dependency is used by the application.
  implementation(libs.guava)

  implementation(files("C:\\Program Files\\Java\\Java3D\\1.5.2\\lib\\ext\\j3dcore.jar"))
  implementation(files("C:\\Program Files\\Java\\Java3D\\1.5.2\\lib\\ext\\j3dutils.jar"))
  implementation(files("C:\\Program Files\\Java\\Java3D\\1.5.2\\lib\\ext\\vecmath.jar"))
}

// Apply a specific Java toolchain to ease working on different environments.
java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

application {
  // Define the main class for the application.
  mainClass = "dev.teelyjc.App"
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

tasks.withType<JavaExec> {
  jvmArgs = listOf("--add-exports", "java.desktop/sun.awt=ALL-UNNAMED")
}

tasks.withType<Test> {
  jvmArgs = listOf("--add-exports", "java.desktop/sun.awt=ALL-UNNAMED")
}
