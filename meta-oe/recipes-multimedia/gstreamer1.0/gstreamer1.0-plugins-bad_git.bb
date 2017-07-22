DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
"

UPSTREAM_CHECK_GITTAGREGEX = "(?P<pver>(\d+(\.\d+)+))"

SRCREV_base = "43335d31c640eb9f4a2df11b92c7c25cad56d85d"
SRCREV_common = "29046b89d80bbca22eb222c18820fb40a4ac5bde"
SRCREV_FORMAT = "base"

SRC_URI = "git://anongit.freedesktop.org/gstreamer/gst-plugins-bad;branch=master;name=base \
           git://anongit.freedesktop.org/gstreamer/common;destsuffix=git/common;name=common \
"


SRC_URI += "file://configure-allow-to-disable-libssh2.patch \
			file://0001-Makefile.am-don-t-hardcode-libtool-name-when-running-pbad.patch \
			file://0001-rtmp-fix-seeking-and-potential-segfault.patch \
			file://0004-rtmp-hls-tsdemux-fix.patch \
			file://fix-maybe-uninitialized-warnings-when-compiling-with-Os.patch \
			file://dvbapi5-fix-old-kernel.patch \
			file://hls-main-thread-block.patch \
			"
S = "${WORKDIR}/git"

GST_VERSION_FULL ="1.13.0.2"
inherit gitpkgv
PV = "${GST_VERSION_FULL}+git${SRCPV}"
PKGV = "${GST_VERSION_FULL}+git${GITPKGV}"


EXTRA_OECONF += " \
    --disable-openjpeg \
    "

do_configure_prepend() {
	${S}/autogen.sh --noconfigure
}

TARGET_CFLAGS_append = " -Wno-error=maybe-uninitialized -Wno-error=redundant-decls"

# In 1.6.2, the "--enable-hls" configure option generated an installable package
# called "gstreamer1.0-plugins-bad-fragmented". In 1.7.1 that HLS plugin package
# has become "gstreamer1.0-plugins-bad-hls". See:
# http://cgit.freedesktop.org/gstreamer/gst-plugins-bad/commit/?id=efe62292a3d045126654d93239fdf4cc8e48ae08

PACKAGESPLITFUNCS_append = " handle_hls_rename "

python handle_hls_rename () {
    d.setVar('RPROVIDES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RREPLACES_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
    d.setVar('RCONFLICTS_gstreamer1.0-plugins-bad-hls', 'gstreamer1.0-plugins-bad-fragmented')
}
