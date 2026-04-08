#!/usr/bin/env bash

set -euo pipefail

if [[ $# -lt 4 || $# -gt 6 ]]; then
  echo "usage: $0 <x64|aarch64> <version> <jar-path> <output-dir> [suffix] [default-java-opts]" >&2
  exit 1
fi

arch="$1"
version="$2"
jar_path="$3"
output_dir="$4"
suffix="${5:-}"
default_java_opts="${6:-}"

case "$arch" in
  x64|aarch64)
    ;;
  *)
    echo "unsupported architecture: $arch" >&2
    exit 1
    ;;
esac

if [[ ! -f "$jar_path" ]]; then
  echo "jar not found: $jar_path" >&2
  exit 1
fi

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd "${script_dir}/.." && pwd)"
dist_name="rocketmq-dashboard-${version}"
if [[ -n "${suffix}" ]]; then
  dist_name="${dist_name}-${suffix}"
fi
dist_name="${dist_name}-linux-${arch}"
work_dir="$(mktemp -d)"
download_url="https://api.adoptium.net/v3/binary/latest/17/ga/linux/${arch}/jre/hotspot/normal/eclipse?project=jdk"

cleanup() {
  rm -rf "${work_dir}"
}
trap cleanup EXIT

mkdir -p "${output_dir}"
curl -fsSL "${download_url}" -o "${work_dir}/jre.tar.gz"
tar -xzf "${work_dir}/jre.tar.gz" -C "${work_dir}"

jre_dir="$(find "${work_dir}" -mindepth 1 -maxdepth 1 -type d -name 'jdk-*' | head -n 1)"
if [[ -z "${jre_dir}" ]]; then
  echo "failed to locate extracted JRE directory" >&2
  exit 1
fi

dist_root="${work_dir}/${dist_name}"
mkdir -p "${dist_root}/bin" "${dist_root}/lib"
cp "${jar_path}" "${dist_root}/lib/rocketmq-dashboard.jar"
cp "${repo_root}/LICENSE" "${repo_root}/NOTICE" "${repo_root}/README.md" "${dist_root}/"
mv "${jre_dir}" "${dist_root}/runtime"

cat > "${dist_root}/bin/rocketmq-dashboard" <<EOF
#!/usr/bin/env sh
set -eu

SCRIPT_DIR=\$(CDPATH= cd -- "\$(dirname -- "\$0")" && pwd)
APP_HOME=\$(CDPATH= cd -- "\${SCRIPT_DIR}/.." && pwd)
DEFAULT_JAVA_OPTS="${default_java_opts}"

exec "\${APP_HOME}/runtime/bin/java" \${DEFAULT_JAVA_OPTS} \${JAVA_OPTS:-} -jar "\${APP_HOME}/lib/rocketmq-dashboard.jar" "\$@"
EOF

chmod +x "${dist_root}/bin/rocketmq-dashboard"

archive_path="${output_dir}/${dist_name}.tar.gz"
tar -czf "${archive_path}" -C "${work_dir}" "${dist_name}"
(cd "${output_dir}" && shasum -a 256 "${dist_name}.tar.gz" > "${dist_name}.tar.gz.sha256")
