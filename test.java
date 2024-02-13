public List<Map<String, Object>> getUncorrelatedIdentities() {
        List<Map<String, Object>> uncorrelatedIdentities = new ArrayList<>();
        List<String> columns = new ArrayList<>();
         try {
            Application app = context.getObjectByName(Application.class, "authnPassword");
              Set<String> seenIds = new HashSet<>();
              Iterator<Link> it = context.search(Link.class, getUncorrelatedIdentitiesQueryOptions(app));
              while (it.hasNext()) {
                Link link = it.next();
                Identity identity = link.getIdentity();
                if (!seenIds.contains(identity.getId())) {
                  Map<String, Object> map = new HashMap<>();
                  if (columns != null && !columns.isEmpty())
                    for (String column : columns) {
                      Object attr = link.getAttribute(column);
                      map.put(column, (attr != null) ? attr.toString() : null);
                    }  
                  map.put("applicationName", app.getName()); 
                  map.put("username", identity.getName());
                  uncorrelatedIdentities.add(map);
                  seenIds.add(identity.getId());
                } 
                context.decache((SailPointObject)link);
              } 
            } catch (GeneralException ge) {
              if (log.isErrorEnabled())
                log.error("Unable to load uncorrelated identities. Exception: " + ge.getMessage(), (Throwable)ge); 
              uncorrelatedIdentities = null;
            }  
          
          return uncorrelatedIdentities;}
          
        private QueryOptions getUncorrelatedIdentitiesQueryOptions(Application application) {
          QueryOptions qo = new QueryOptions();
          qo.add(new Filter[] { Filter.and(Filter.eq("application.id", application.getId()), 
                  Filter.eq("identity.correlated", Boolean.valueOf(false))) });
          return qo;
        
      } 
