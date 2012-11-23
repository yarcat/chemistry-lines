##
## The file must be included at the beginning of all code generation templates.
##
<% import sys, textwrap %>\
/* Generation parameters
 * ---------------------
 * sys.argv:
% for line in textwrap.fill(" ".join(sys.argv)).splitlines():
 * ${line}
% endfor
 *
 * argparse result:
% for name in dir(PARSED_ARGS):
    % if not name.startswith("_"):
<% t = " {:<20} {}".format(name, getattr(PARSED_ARGS, name))%>\
% for line in textwrap.fill(t, subsequent_indent=" "*22).splitlines():
 * ${line}
% endfor
    % endif
% endfor
 *
 */

## vim: set ft=mako :
