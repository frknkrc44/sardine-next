# Sardine next
> A WebDAV client for Android, using [OkHttp](https://github.com/square/okhttp) as HTTP client.

[![Android CI](https://github.com/XayahSuSuSu/sardine-next/actions/workflows/android.yml/badge.svg)](https://github.com/XayahSuSuSu/sardine-next/actions/workflows/android.yml) [![JitPack](https://jitpack.io/v/xayahsususu/sardine-next.svg)](https://jitpack.io/#xayahsususu/sardine-next)  ![minSdk](https://img.shields.io/badge/minSdk-21-green) [![License](https://img.shields.io/github/license/XayahSuSuSu/sardine-next?color=ff69b4)](./LICENSE)

## Getting started
1. Edit `settings.gradle`/`settings.gradle.kts`
* **Groovy**
```
repositories {
    // ......
    maven { url 'https://jitpack.io' }
}
```
* **Kotlin**
```
repositories {
    // ......
    maven("https://jitpack.io")
}
```
2. Implementation
* **Groovy**
```
implementation 'com.github.xayahsususu:sardine-next:$version'
```

* **Kotlin**
```
implementation("com.github.xayahsususu:sardine-next:$version")
```

- Create a `Sardine` client:
```
Sardine sardine = new OkHttpSardine();
sardine.setCredentials("username", "password");
```

- Use the client to make requests to your WebDAV server:
```
List<DavResource> resources = sardine.list("http://webdav.server.com");
```

## Legacy
Originally forked from [sardine](https://github.com/lookfirst/sardine), [sardine-android](https://github.com/thegrizzlylabs/sardine-android).

[Apache HTTP Client](http://hc.apache.org/) was replaced by [okhttp](https://github.com/square/okhttp).

JAXB was replaced by [SimpleXml](http://simple.sourceforge.net/).

## Alternatives
See [Awesome WebDAV: Java libraries](https://github.com/WebDAVDevs/awesome-webdav/blob/main/readme.md#java).
