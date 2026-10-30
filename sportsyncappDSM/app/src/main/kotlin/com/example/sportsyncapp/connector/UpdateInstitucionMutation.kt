
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector



public interface UpdateInstitucionMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      UpdateInstitucionMutation.Data,
      UpdateInstitucionMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val id: @kotlinx.serialization.Serializable(with = com.google.firebase.dataconnect.serializers.UUIDSerializer::class) java.util.UUID,
  
    val nombre: String,
  
    val logo: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
    val descripcion: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var id: java.util.UUID
        public var nombre: String
        public var logo: String?
        public var descripcion: String?
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          id: java.util.UUID,nombre: String,
          block_: Builder.() -> Unit
        ): Variables {
          var id= id
            var nombre= nombre
            var logo: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var descripcion: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            

          return object : Builder {
            override var id: java.util.UUID
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { id = value_ }
              
            override var nombre: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { nombre = value_ }
              
            override var logo: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { logo = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var descripcion: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { descripcion = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              id=id,nombre=nombre,logo=logo,descripcion=descripcion,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val instituciones_update: InstitucionesKey?,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "UpdateInstitucion"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun UpdateInstitucionMutation.ref(
  
    id: java.util.UUID,nombre: String,

  
    block_: UpdateInstitucionMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    UpdateInstitucionMutation.Data,
    UpdateInstitucionMutation.Variables
  > =
  ref(
    
      UpdateInstitucionMutation.Variables.build(
        id=id,nombre=nombre,
  
    block_
      )
    
  )

public suspend fun UpdateInstitucionMutation.execute(

  
    
      id: java.util.UUID,nombre: String,

  
    block_: UpdateInstitucionMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    UpdateInstitucionMutation.Data,
    UpdateInstitucionMutation.Variables
  > =
  ref(
    
      id=id,nombre=nombre,
  
    block_
    
  ).execute()


