awk 'FNR==1 && /^[^/]/ {
            print "/*"
            print " * Copyright (c) 2009-2014, ZawodyWeb Team"
            print " * All rights reserved."
            print " *"
            print " * This file is distributable under the Simplified BSD license. See the terms"
            print " * of the Simplified BSD license in the documentation provided with this file."
            print " */"
            print $0
            }
     FNR==1 && /^\/\\*/ {
              while (getline) {
                if ($0 ~ "[ ]*\*/") { getline ; break }
              }
            print "/*"
            print " * Copyright (c) 2009-2014, ZawodyWeb Team"
            print " * All rights reserved."
            print " *"
            print " * This file is distributable under the Simplified BSD license. See the terms"
            print " * of the Simplified BSD license in the documentation provided with this file."
            print " */"
            }
     FNR>1  { print $0 }' $1 > $1.new

# find -name "*.java" -exec ./license.sh {} \;
# find -type f -name '*.java.new' | while read f; do \mv "$f" "${f%.new}"; done
