# ∫ Integral Quartz

Use th [Quartz scheduler](http://www.quartz-scheduler.org/) easily from Tapestry.

## Why?

We're deeply committed to Tapestry as our daily driver so we like to build some
frameworks to enhance the experience, and now we started to share some of our
stuff under the name _integral_.

## Usage

NOTE: This library isn't released yet on jcenter etc.!

### `build.gradle`:
```groovy
respositories {
  jcenter()
}

dependencies {
    compile "net.netzgut.integral:integral-quartz:0.0.1"
}

```

Now just import `QuartzModule.class` in your app module.. TODO: How to use this?

- add job
- configuration
- note on failing tasks / repeat 
- how to remove / update jobs with different triggers
- start date
- one job <-> one trigger
- tests
 MDC.put("requestedUrl", jobClazz.getName());     MDC.clear();

## Gradle task uploadArchives

To upload the archives you need to set some project properties:

- snapshot_repository
- snapshot_repository_username
- snapshot_repository_password

The fallbacks are empty strings, so you can build etc. without gradle failing instantly.


## Contribute

If you want to contribute feel free to open issues or provide pull requests. Please read the additional info in the folder `_CONTRIBUTE`.


## License

Apache 2.0 license, see `LICENSE.txt` and `NOTICE.txt` for more details.
