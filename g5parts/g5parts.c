/*
**
** Copyright 2013, Davinder Singh (@DSM_)
**
** Licensed under the Apache License, Version 2.0 (the "License"); 
** you may not use this file except in compliance with the License. 
** You may obtain a copy of the License at 
**
**     http://www.apache.org/licenses/LICENSE-2.0 
**
** Unless required by applicable law or agreed to in writing, software 
** distributed under the License is distributed on an "AS IS" BASIS, 
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
** See the License for the specific language governing permissions and 
** limitations under the License.
*/

#include <stdio.h>

int main(int argc, char **argv)
{
	if(setgid(0) || setuid(0) || setgroups(0, NULL))
	{
		printf("Galaxy5Parts: Permission denied\n");
		return 1;
	}

	char *cmd[] = { "sh", (char *)0 };
	execv("/system/bin/sh", cmd);
	return 0;
}