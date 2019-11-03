/**
 *  Represents OSM's relation class
 *  
 *  @author:     José Ferro Pinto (233843)
 *  @author:     Dorian Laforest (234832)
 */
package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class OSMRelation extends OSMEntity {
    final private List<Member> members;

    /**
     * Constructor for OSMRelation
     * @param id unique ID from OSM
     * @param members a list of the members
     * @param attributes the relation attributes
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(members));
    }

    /**
     * Returns the list of members
     * @return the list of members
     */
    public List<Member> members() {
        return members;
    }

    /**
     *  OSMRelation.Member class
     *  Represents a member of the relation
     *  
     *  @author:     José Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public final static class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;

        /**
         * Constructor for OSMRelation.Member
         * @param type the member's type
         * @param role the member's role
         * @param member the relation's member
         */
        public Member(Type type, String role, OSMEntity member){
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * Returns the member's type
         * @return the member's type
         */
        public Type type() {
            return type;
        }

        /**
         * Returns the member's role
         * @return the member's role
         */
        public String role() {
            return role;
        }

        /**
         * Returns the member itself
         * @return the member itself
         */
        public OSMEntity member() {
            return member;
        }

        /**
         *  OSMRelation.Member.Type enumeration
         *  Represents the three types a relation can have, NODE, WAY and RELATION
         *  
         *  @author:     José Ferro Pinto (233843)
         *  @author:     Dorian Laforest (234832)
         */
        public static enum Type {
            NODE, WAY, RELATION
        }
    }

    /**
     *  Represents OSMRelation's Builder class
     *  
     *  @author:     José Ferro Pinto (233843)
     *  @author:     Dorian Laforest (234832)
     */
    public final static class Builder extends OSMEntity.Builder {
        private List<Member> member;

        /**
         * Constructor for OSMRelation.Builder
         * @param id unique ID from OSM
         */
        public Builder(long id) {
            super(id);
            member = new ArrayList<>();
        }

        /**
         * Adds a member to the relation
         * @param type the member's type
         * @param role the member's role
         * @param newMember the new member to add
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            member.add(new Member(type, role, newMember));
        }

        /**
         * Builds and returns a new OSMRelation
         * @return a new OSMRelation built
         * @throws IllegalStateException if incomplete
         */
        public OSMRelation build() throws IllegalStateException{
            if(isIncomplete()) {
                throw new IllegalStateException("Incomplete relation");
            }
            return new OSMRelation(super.id, member, super.attributes.build());
        }
    }
}